package cn.gdeveloper.mapchat.parser;

import android.content.SharedPreferences;
import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.apache.http.Header;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;

import cn.gdeveloper.mapchat.app.DemoContext;
import me.add1.exception.InternalException;
import me.add1.exception.ParseException;

public class GsonParser<T extends Serializable> extends JsonObjectParser<T> {

    Type type;
    Gson gson;

    public GsonParser(Class<T> type) {
        gson = new Gson();
        this.type = type;
    }

    @Override
    public T jsonParse(JsonReader reader) throws JSONException, IOException, ParseException, InternalException,JsonSyntaxException {
        return gson.fromJson(String.valueOf(reader), this.type);
    }

    @Override
    public void onHeaderParsed(Header[] headers) {

        if (headers == null) {
            return;
        } else {
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].getName().equals("Set-Cookie")) {
                    String[] cookievalues = headers[i].getValue().split(";");
                    SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
                    edit.putString("DEMO_COOKIE", cookievalues[0]);
                    edit.apply();
                }
            }
        }
    }


}

