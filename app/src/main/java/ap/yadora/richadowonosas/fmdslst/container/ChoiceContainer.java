package ap.yadora.richadowonosas.fmdslst.container;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import ap.yadora.richadowonosas.fmdslst.R;

final class ChoiceContainer {
    public static final int[] CHOICE_ID = {
            R.id.editHikari,
            R.id.editTairitsu
    };

    private final View root;
    private final TextView title;
    private final RadioGroup choice;
    private final RadioButton hikari, tairitsu;

    public ChoiceContainer(View view, int rootId) {
        this.root = view.findViewById(rootId);

        title = root.findViewById(R.id.editTitleChoice);
        choice = root.findViewById(R.id.editChoice);
        hikari = root.findViewById(R.id.editHikari);
        tairitsu = root.findViewById(R.id.editTairitsu);
    }

    public View getRoot() {
        return root;
    }

    public TextView getTitle() {
        return title;
    }

    public RadioGroup getChoice() {
        return choice;
    }

    public RadioButton getHikari() {
        return hikari;
    }

    public RadioButton getTairitsu() {
        return tairitsu;
    }

    public int getSelected() {
        int viewId = choice.getCheckedRadioButtonId();
        for (int i = 0; i < CHOICE_ID.length; i++) {
            if (CHOICE_ID[i] == viewId) {
                return i;
            }
        }
        return -1;
    }
}
