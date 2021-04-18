package ap.yadora.richadowonosas.fmdslst.container;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ap.yadora.richadowonosas.fmdslst.R;

final class SingleContainer {
    public static final int TYPE_PLAIN_TEXT = 0;
    public static final int TYPE_MULTILINE_TEXT = 1;
    public static final int TYPE_INTEGER = 2;
    public static final int TYPE_DECIMAL = 3;

    private final int type;
    private final View root;
    private final TextView title;
    private final EditText content;

    public SingleContainer(View view, int rootId, int type) {
        this.type = type;
        root = view.findViewById(rootId);

        title = root.findViewById(R.id.editTitleSingle);
        content = root.findViewById(R.id.editContentSingle);

        switch(type) {
            default:
            case TYPE_PLAIN_TEXT:
                content.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case TYPE_MULTILINE_TEXT:
                content.setInputType(InputType.TYPE_CLASS_TEXT ^ InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                break;
            case TYPE_INTEGER:
                content.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case TYPE_DECIMAL:
                content.setInputType(InputType.TYPE_CLASS_NUMBER ^ InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
        }
    }

    public View getRoot() {
        return root;
    }

    public int getType() {
        return type;
    }

    public TextView getTitle() {
        return title;
    }

    public EditText getContent() {
        return content;
    }
}
