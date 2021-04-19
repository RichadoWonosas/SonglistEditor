package ap.yadora.richadowonosas.fmdslst.container;

import android.view.View;
import android.widget.TextView;

import ap.yadora.richadowonosas.fmdslst.R;

final class DaynightContainer {
    private final View root;
    private final TextView title;
    private final SingleContainer day, night;

    public DaynightContainer(View view, int rootId) {
        root = view.findViewById(rootId);

        title = root.findViewById(R.id.editTitleDaynight);
        day = new SingleContainer(root, R.id.editDay, SingleContainer.TYPE_PLAIN_TEXT);
        night = new SingleContainer(root, R.id.editNight, SingleContainer.TYPE_PLAIN_TEXT);

        day.getTitle().setText(R.string.info_daynight_day);
        night.getTitle().setText(R.string.info_daynight_night);
    }

    public String[] getDaynightStrings() {
        return ((day.getContent().getText().length() == 0) &&
                (night.getContent().getText().length() == 0)) ?
                null :
                new String[]{
                        day.getContent().getText().toString(),
                        night.getContent().getText().toString()
                };
    }

    public View getRoot() {
        return root;
    }

    public TextView getTitle() {
        return title;
    }

    public SingleContainer getDay() {
        return day;
    }

    public SingleContainer getNight() {
        return night;
    }
}
