package br.ufpe.cin.if710.podcast.ui.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.nsd.NsdManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.Util;
import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProvider;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.services.DownloadXMLService;
import br.ufpe.cin.if710.podcast.ui.EpisodeDetailActivity;

public class XmlFeedAdapter extends ArrayAdapter<ItemFeed> {

    Context mContext;
    int linkResource;
    ViewHolder holder;

    public XmlFeedAdapter(Context context, int resource, List<ItemFeed> objects) {
        super(context, resource, objects);
        mContext = context;
        linkResource = resource;
    }

    /**
     * public abstract View getView (int position, View convertView, ViewGroup parent)
     * <p>
     * Added in API level 1
     * Get a View that displays the data at the specified position in the data set. You can either create a View manually or inflate it from an XML layout file. When the View is inflated, the parent View (GridView, ListView...) will apply default layout parameters unless you use inflate(int, android.view.ViewGroup, boolean) to specify a root view and to prevent attachment to the root.
     * <p>
     * Parameters
     * position	The position of the item within the adapter's data set of the item whose view we want.
     * convertView	The old view to reuse, if possible. Note: You should check that this view is non-null and of an appropriate type before using. If it is not possible to convert this view to display the correct data, this method can create a new view. Heterogeneous lists can specify their number of view types, so that this View is always of the right type (see getViewTypeCount() and getItemViewType(int)).
     * parent	The parent that this view will eventually be attached to
     * Returns
     * A View corresponding to the data at the specified position.
     */


	/*
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.itemlista, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.item_title);
		textView.setText(items.get(position).getTitle());
	    return rowView;
	}
	/**/

    //http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder

    public class MyOnCompletedListener implements MediaPlayer.OnCompletionListener{

        ItemFeed item;
        Button btn;

        public MyOnCompletedListener(Button btn, ItemFeed item){
            this.btn = btn;
            this.item = item;
        }

        public void onCompletion(MediaPlayer mp){
            btn.setText("Download");
            btn.setBackgroundColor(Color.GREEN);

            File file = new File(item.getUri());
            file.delete();

            ContentValues cv = new ContentValues();
            cv.put(PodcastDBHelper.EPISODE_FILE_URI, "NONE");
            String selection = PodcastProviderContract.EPISODE_LINK + " = ?";
            String[] selection_args = new String[]{item.getLink()};
            mContext.getContentResolver().update(PodcastProviderContract.EPISODE_LIST_URI, cv, selection, selection_args);
        }
    }

    public class MyOnClickListener implements View.OnClickListener{

        ItemFeed currentItem;
        Context mContext;
        Uri item_uri;

        public MyOnClickListener(ItemFeed item, Context context, Uri uri){
            currentItem = item;
            mContext = context;
            item_uri = uri;
        }


        public void onClick(View view) {
            if ((currentItem.getUri()).equals("NONE")) {
                String item_download_link = currentItem.getDownloadLink();
                if (Util.isNetworkAvailable(mContext)) {
                    DownloadXMLService.startActionDownloadPodcast(mContext, currentItem);
                    ((Button)view).setText("Downloading");
                    ((Button)view).setBackgroundColor(Color.BLUE);
                }
                else {
                    Toast.makeText(mContext, "There isn't a internet connection", Toast.LENGTH_SHORT).show();
                }
            } else {
                fixMediaPlayer(item_uri, currentItem);
                MediaPlayer mediaPlayer = Util.getMPReg().get(currentItem.getLink());
                if (((Button)view).getText() == "Play" ) {
                    mediaPlayer.start();
                    ((Button)view).setText("Pause");
                    ((Button)view).setBackgroundColor(Color.GRAY);
                }

                else if (((Button)view).getText() == "Continue") {
                    Log.d("Adapter", currentItem.getCurrentTime()+"");
                    mediaPlayer.seekTo(currentItem.getCurrentTime());
                    mediaPlayer.start();
                    ((Button)view).setText("Pause");
                    ((Button)view).setBackgroundColor(Color.GRAY);
                }

                else if (((Button)view).getText() == "Pause") {
                    mediaPlayer.pause();
                    ((Button)view).setText("Continue");
                    ((Button)view).setBackgroundColor(Color.GREEN);

                    currentItem.setCurrentTime(mediaPlayer.getCurrentPosition());

                    ContentValues cv = new ContentValues();
                    cv.put(PodcastDBHelper.EPISODE_CURRENT_TIME, "" + currentItem.getCurrentTime());
                    String selection = PodcastProviderContract.EPISODE_LINK + " = ?";
                    String[] selection_args = new String[]{currentItem.getLink()};
                    mContext.getContentResolver().update(PodcastProviderContract.EPISODE_LIST_URI, cv, selection, selection_args);
                }
            }
        }
    }

    static class ViewHolder {
        TextView item_title;
        TextView item_date;
        Button item_Btn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ItemFeed currentItem = getItem(position);
        if (convertView == null) {
            convertView = View.inflate(getContext(), linkResource, null);
            this.holder = new ViewHolder();
            this.holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            this.holder.item_date = (TextView) convertView.findViewById(R.id.item_date);
            this.holder.item_Btn = convertView.findViewById(R.id.item_action);

            convertView.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) convertView.getTag();
        }
        final ItemFeed item = getItem(position);
        this.holder.item_title.setText(currentItem.getTitle());
        this.holder.item_date.setText(currentItem.getPubDate());

        if (!(currentItem.getUri()).equals("NONE")) {
            this.holder.item_Btn.setEnabled(true);
            MediaPlayer player = Util.getMPReg().get(currentItem.getLink());
            if (player != null && player.isPlaying()) {
                this.holder.item_Btn.setText("Pause");
                this.holder.item_Btn.setBackgroundColor(Color.GRAY);
            }else if (currentItem.getCurrentTime() == 0){
                this.holder.item_Btn.setText("Play");
                this.holder.item_Btn.setBackgroundColor(Color.GREEN);
            }else{
                this.holder.item_Btn.setText("Continue");
                this.holder.item_Btn.setBackgroundColor(Color.GREEN);
            }
        }
        else {
            Log.d("Adapter", currentItem.getTitle()+" "+currentItem.getUri());
            if(DownloadXMLService.isDownloading && DownloadXMLService.currentDownloadingItem.equals(currentItem.getDownloadLink())){
                this.holder.item_Btn.setText("Downloading");
                this.holder.item_Btn.setBackgroundColor(Color.BLUE);
            }else {
                this.holder.item_Btn.setText("Download");
                this.holder.item_Btn.setBackgroundColor(mContext.getResources().getColor(R.color.Orange));
            }
        }

        this.holder.item_title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Context context = getContext();
                Intent intent = new Intent(context, EpisodeDetailActivity.class);
                intent.putExtra(PodcastDBHelper.EPISODE_TITLE, item.getTitle());
                intent.putExtra(PodcastDBHelper.EPISODE_LINK, item.getLink());
                intent.putExtra(PodcastDBHelper.EPISODE_DESC, item.getDescription());
                intent.putExtra(PodcastDBHelper.EPISODE_DATE, item.getPubDate());
                intent.putExtra(PodcastDBHelper.EPISODE_DOWNLOAD_LINK, item.getDownloadLink());
                intent.putExtra(PodcastDBHelper.EPISODE_FILE_URI, item.getUri());
                intent.putExtra(PodcastDBHelper.EPISODE_CURRENT_TIME, item.getCurrentTime());

                context.startActivity(intent);
            }
        });
        final Uri item_uri = Uri.parse(currentItem.getUri());
        Log.d("Adapter","Boolean: "+!(item_uri.toString().equals("NONE")));


        this.holder.item_Btn.setOnClickListener(new MyOnClickListener(currentItem, mContext, item_uri));

        return convertView;
    }

    public void fixMediaPlayer(Uri item_uri, ItemFeed currentItem){
        if (Util.getMPReg().get(currentItem.getLink())==null && !(item_uri.toString().equals("NONE"))) {
            Log.d("Adapter","enter");
            MediaPlayer mediaPlayer = MediaPlayer.create(mContext, item_uri);
            mediaPlayer.setLooping(false);
            mediaPlayer.setOnCompletionListener(new MyOnCompletedListener(holder.item_Btn, currentItem));
            Util.addReg(currentItem.getLink(), mediaPlayer);
            //todo:tratar erro de quando o cara apaga o podcast
        }
    }

}