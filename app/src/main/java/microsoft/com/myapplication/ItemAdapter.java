package microsoft.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<JSONObject> {



    private ImageLoader mImageLoader;
    Context context;
    int layoutResourceId;
     ArrayList<JSONObject> data = null;

    public ItemAdapter(Context context, int layoutResourceId, ArrayList<JSONObject> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemHolder();
            holder.imgIcon = (NetworkImageView)row.findViewById(R.id.networkImageView);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)row.getTag();
        }

        JSONObject item  = data.get(position);
        try {
            holder.txtTitle.setText(item.getString("title"));
            holder.imgIcon.setImageUrl(item.getJSONObject("thumbnail").getString("source"), mImageLoader);
        } catch (JSONException e) {

        }


        return row;
    }

    static class ItemHolder
    {
        NetworkImageView imgIcon;
        TextView txtTitle;
    }
}