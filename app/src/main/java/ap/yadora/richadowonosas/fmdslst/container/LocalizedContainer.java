package ap.yadora.richadowonosas.fmdslst.container;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import ap.yadora.richadowonosas.fmdslst.R;
import ap.yadora.richadowonosas.fmdslst.song.SongInfo;

final class LocalizedContainer {
    private final View root;
    private final Button expandButton;
    private final TextView title, contentTitle;
    private final SingleContainer en, ja, zhHans, zhHant, ko;
    private boolean isExpanded;
    private int textHeight;
    private final String defaultTitle;

    public LocalizedContainer(View view, int rootId) {
        root = view.findViewById(rootId);
        textHeight = 0;
        defaultTitle = root.getContext().getString(R.string.default_song_title);

        title = root.findViewById(R.id.editTitleLocalized);
        contentTitle = root.findViewById(R.id.contentTitle);
        expandButton = root.findViewById(R.id.editButtonExpandLocalized);
        en = new SingleContainer(root, R.id.editEn, SingleContainer.TYPE_MULTILINE_TEXT);
        ja = new SingleContainer(root, R.id.editJa, SingleContainer.TYPE_MULTILINE_TEXT);
        zhHans = new SingleContainer(root, R.id.editZhHans, SingleContainer.TYPE_MULTILINE_TEXT);
        zhHant = new SingleContainer(root, R.id.editZhHant, SingleContainer.TYPE_MULTILINE_TEXT);
        ko = new SingleContainer(root, R.id.editKo, SingleContainer.TYPE_MULTILINE_TEXT);
        expandButton.setText(R.string.selection_expand);

        isExpanded = false;
        en.getTitle().setText(R.string.info_localized_en);
        ja.getTitle().setText(R.string.info_localized_ja);
        zhHans.getTitle().setText(R.string.info_localized_zh_hans);
        zhHant.getTitle().setText(R.string.info_localized_zh_hant);
        ko.getTitle().setText(R.string.info_localized_ko);

        setHeight(en.getRoot(), textHeight);
        setHeight(ja.getRoot(), textHeight);
        setHeight(zhHans.getRoot(), textHeight);
        setHeight(zhHant.getRoot(), textHeight);
        setHeight(ko.getRoot(), textHeight);

        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                refresh();
            }
        };
        en.getContent().setOnFocusChangeListener(onFocusChangeListener);
        ja.getContent().setOnFocusChangeListener(onFocusChangeListener);
        zhHans.getContent().setOnFocusChangeListener(onFocusChangeListener);
        zhHant.getContent().setOnFocusChangeListener(onFocusChangeListener);
        ko.getContent().setOnFocusChangeListener(onFocusChangeListener);

        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded = !isExpanded;
                if (isExpanded) {
                    expandButton.setText(R.string.selection_collapse);
                    expandButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, root.getContext().getDrawable(R.drawable.icon_normal_expand), null);
                    textHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
                } else {
                    expandButton.setText(R.string.selection_expand);
                    expandButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, root.getContext().getDrawable(R.drawable.icon_normal_collapse), null);
                    textHeight = 0;
                }

                setHeight(en.getRoot(), textHeight);
                setHeight(ja.getRoot(), textHeight);
                setHeight(zhHans.getRoot(), textHeight);
                setHeight(zhHant.getRoot(), textHeight);
                setHeight(ko.getRoot(), textHeight);
            }
        });
    }

    private void setHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public void refresh() {
        contentTitle.setText(getStringToShow());
    }

    public String[] getLocalizedStrings(boolean allowsNull) {
        return (allowsNull &&
                (en.getContent().getText().length() == 0) &&
                (ja.getContent().getText().length() == 0) &&
                (zhHans.getContent().getText().length() == 0) &&
                (zhHant.getContent().getText().length() == 0) &&
                (ko.getContent().getText().length() == 0)) ?
                null :
                new String[]{
                        en.getContent().getText().toString(),
                        (ja.getContent().getText().length() == 0) ? null : ja.getContent().getText().toString(),
                        (zhHans.getContent().getText().length() == 0) ? null : zhHans.getContent().getText().toString(),
                        (zhHant.getContent().getText().length() == 0) ? null : zhHant.getContent().getText().toString(),
                        (ko.getContent().getText().length() == 0) ? null : ko.getContent().getText().toString()
                };
    }

    private String getStringToShow() {
        String result = null;
        String[] localizedStrings = getLocalizedStrings(true);
        if (localizedStrings == null) {
            return defaultTitle;
        }
        
        switch (Locale.getDefault().getLanguage()) {
            case "zh":
                if (Locale.getDefault().getCountry().equals("CN")) {
                    result = localizedStrings[SongInfo.LOCALIZED_ZH_HANS];
                }
                else {
                    result = localizedStrings[SongInfo.LOCALIZED_ZH_HANT];
                }
                break;
            case "ko":
                result = localizedStrings[SongInfo.LOCALIZED_KO];
                break;
            case "ja":
                result = localizedStrings[SongInfo.LOCALIZED_JA];
                break;
            case "en":
                result = localizedStrings[SongInfo.LOCALIZED_EN];
        }
        if (result == null) {
            if ((result = localizedStrings[SongInfo.LOCALIZED_JA]) == null) {
                result = localizedStrings[SongInfo.LOCALIZED_EN];
            }
        }

        return result;
    }

    public View getRoot() {
        return root;
    }

    public TextView getTitle() {
        return title;
    }

    public SingleContainer getEn() {
        return en;
    }

    public SingleContainer getJa() {
        return ja;
    }

    public SingleContainer getZhHans() {
        return zhHans;
    }

    public SingleContainer getZhHant() {
        return zhHant;
    }

    public SingleContainer getKo() {
        return ko;
    }
}
