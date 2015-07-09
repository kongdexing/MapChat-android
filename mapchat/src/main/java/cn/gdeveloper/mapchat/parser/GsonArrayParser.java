package cn.gdeveloper.mapchat.parser;

import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.Header;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import me.add1.exception.InternalException;
import me.add1.exception.ParseException;

public class GsonArrayParser<T extends Serializable> extends JsonObjectParser<ArrayList<T>> {

    TypeToken<ArrayList<T>> type;
    Gson gson;

    public GsonArrayParser(TypeToken<ArrayList<T>> typeToken) {
        gson = new Gson();
        this.type = typeToken;
    }

    @Override
    public ArrayList<T> jsonParse(JsonReader reader) throws JSONException, IOException, ParseException, InternalException {
        return gson.fromJson(String.valueOf(reader), type.getType());
    }


    @Override
    public void onHeaderParsed(Header[] headers) {

    }
}
