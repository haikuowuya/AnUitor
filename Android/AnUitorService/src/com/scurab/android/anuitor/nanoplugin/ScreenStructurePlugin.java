package com.scurab.android.anuitor.nanoplugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.scurab.android.anuitor.extract.ActivityExtractor;
import com.scurab.android.anuitor.extract.BaseExtractor;
import com.scurab.android.anuitor.extract.FragmentActivityExtractor;
import com.scurab.android.anuitor.extract.ViewDetailExtractor;
import com.scurab.android.anuitor.reflect.WindowManagerGlobal;
import com.scurab.android.anuitor.tools.HttpTools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.iki.elonen.NanoHTTPD;

import static com.scurab.android.anuitor.tools.HttpTools.MimeType.APP_JSON;

/**
 * Created by jbruchanov on 24/06/2014.
 */
public class ScreenStructurePlugin extends BasePlugin {

    public static final String FILE = "screenstructure.json";
    public static final String PATH = "/" + FILE;

    private WindowManagerGlobal mWindowManager;

    public ScreenStructurePlugin(WindowManagerGlobal windowManager) {
        mWindowManager = windowManager;
    }

    @Override
    public String[] files() {
        return new String[]{FILE};
    }

    @Override
    public String mimeType() {
        return HttpTools.MimeType.APP_JSON;
    }

    @Override
    public boolean canServeUri(String uri, File rootDir) {
        return PATH.equals(uri);
    }

    @Override
    public NanoHTTPD.Response serveFile(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session, File file, String mimeType) {
        String[] viewRootNames = mWindowManager.getViewRootNames();
        String json = "{}";

        List<HashMap<String, Object>> resultDataSet = new ArrayList<HashMap<String, Object>>();

        for (String rootName : viewRootNames) {
            View v = mWindowManager.getRootView(rootName);
            Context c = v.getContext();
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("RootName", rootName);
            resultDataSet.add(data);

            if (c instanceof Activity) {
                ActivityExtractor ae;
                Activity a = (Activity) c;
                if (c instanceof FragmentActivity) {
                    ae = new FragmentActivityExtractor();
                } else {
                    ae = new ActivityExtractor();
                }
                ae.fillValues(a, data, null);
            } else {
                BaseExtractor<View> extractor = ViewDetailExtractor.getExtractor(v);
                extractor.fillValues(v, data, null);
            }
        }

        Collections.reverse(resultDataSet);//stack order
        json =  GSON.toJson(resultDataSet);
        NanoHTTPD.Response response = new OKResponse(APP_JSON, new ByteArrayInputStream(json.getBytes()));
        return response;
    }

    public static String describe(Intent intent) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        String s = null;
        if (intent != null) {
            s = describe(intent.getExtras());
        }
        return s;
    }

    public static String describe(Bundle extras) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        String s = null;
        if (extras != null) {
            Set<String> strings = extras.keySet();
            for (String key : strings) {
                Object o = extras.get(key);
                data.put(key, o);
            }
            s = GSON.toJson(data);
        }
        return s;
    }
}