package ap.yadora.richadowonosas.fmdslst.container;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import ap.yadora.richadowonosas.fmdslst.R;

final class CheckContainer {
    private final View root;
    private final TextView title;
    private final CheckBox content;

    public CheckContainer(View view, int rootId) {
        root = view.findViewById(rootId);

        title = root.findViewById(R.id.editTitleCheck);
        content = root.findViewById(R.id.editContentCheck);

        content.setHighlightColor(ContextCompat.getColor(root.getContext(), R.color.tairitsuLight));
    }

    public View getRoot() {
        return root;
    }

    public TextView getTitle() {
        return title;
    }

    public CheckBox getContent() {
        return content;
    }
}
