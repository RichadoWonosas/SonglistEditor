package ap.yadora.richadowonosas.fmdslst.util;

import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Locale;

import ap.yadora.richadowonosas.fmdslst.R;
import ap.yadora.richadowonosas.fmdslst.activity.SelectSongActivity;
import ap.yadora.richadowonosas.fmdslst.song.SongInfo;
import ap.yadora.richadowonosas.fmdslst.song.Songlist;

public final class SongInfoAdapter extends RecyclerView.Adapter<SongInfoAdapter.ViewHolder> {

    private String[] songIds;
    private OnItemClickListener onItemClickListener;

    private int sortCriterion;
    private boolean isAscend;

    public SongInfoAdapter(int sortFactor, boolean isAscend) {
        this.sortCriterion = sortFactor;
        this.isAscend = isAscend;
        songIds = Songlist.songlist.getSongs(sortFactor, isAscend);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_song_selection, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String songTitleString = null;
        String songIdString = null;
        String[] songTitles = SongInfo.getLocalizedStrings(Songlist.songlist.getSongInfoById(songIds[position]).getTitle_localized());
        switch (Locale.getDefault().getLanguage()) {
            case "zh":
                if (Locale.getDefault().getCountry().equals("CN")) {
                    songTitleString = songTitles[SongInfo.LOCALIZED_ZH_HANS];
                }
                else {
                    songTitleString = songTitles[SongInfo.LOCALIZED_ZH_HANT];
                }
                break;
            case "ko":
                songTitleString = songTitles[SongInfo.LOCALIZED_KO];
                break;
            case "ja":
                songTitleString = songTitles[SongInfo.LOCALIZED_JA];
                break;
            case "en":
                songTitleString = songTitles[SongInfo.LOCALIZED_EN];
        }
        if (songTitleString == null) {
            if ((songTitleString = songTitles[SongInfo.LOCALIZED_JA]) == null) {
                songTitleString = songTitles[SongInfo.LOCALIZED_EN];
            }
        }

        holder.getSongTitlePreview().setText(songTitleString);

        songIdString = "(id: " + Songlist.songlist.getSongInfoById(songIds[position]).getId() + ")";
        holder.getSongIdPreview().setText(songIdString);

        File imageFile = new File(Songlist.songlist.getSonglistFileParent(), songIds[position] + "/base.jpg");
        if (imageFile.canRead()) {
            holder.getSongCoverPreview().setImageURI(Uri.fromFile(imageFile));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, position);
                }
                return true;
            }
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_OUTSIDE:
                        holder.isTouching = true;
                        break;
                    default:
                        holder.isTouching = false;
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return Songlist.songlist.getSongAmount();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void refresh() {
        this.songIds = Songlist.songlist.getSongs(sortCriterion, isAscend);
        notifyDataSetChanged();
    }

    public void setSorting(int sortCriterion, boolean isAscend) {
        this.sortCriterion = sortCriterion;
        this.isAscend = isAscend;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TableRow songPreviewFrame;
        private final ImageView songCoverPreview;
        private final TextView songTitlePreview;
        private final TextView songIdPreview;
        private boolean isTouching = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songPreviewFrame = itemView.findViewById(R.id.songPreviewFrame);
            songCoverPreview = itemView.findViewById(R.id.songCoverPreview);
            songTitlePreview = itemView.findViewById(R.id.songTitlePreview);
            songIdPreview = itemView.findViewById(R.id.songIdPreview);

            final Handler handler = new Handler();
            handler.post(new Runnable() {
                int alpha = 0x00;
                final int color = 0xcccccc;

                @Override
                public void run() {
                    if (isTouching) {
                        if ((alpha += 0x24) > 0xFF) {
                            alpha = 0xFF;
                        }
                    } else {
                        if ((alpha -= 0x18) < 0x00) {
                            alpha = 0x00;
                        }
                    }
                    songPreviewFrame.setBackgroundColor((alpha << 24) ^ color);

                    handler.postDelayed(this, 16);
                }

            });
        }

        public ImageView getSongCoverPreview() {
            return songCoverPreview;
        }

        public TextView getSongTitlePreview() {
            return songTitlePreview;
        }

        public TextView getSongIdPreview() {
            return songIdPreview;
        }
    }
}
