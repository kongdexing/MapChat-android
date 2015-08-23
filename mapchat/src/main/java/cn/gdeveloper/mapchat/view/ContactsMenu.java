package cn.gdeveloper.mapchat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.oguzdev.circularfloatingactionmenu.library.animation.SlideInAnimationHandler;

import cn.gdeveloper.mapchat.R;

/**
 * Created by Dexing on 2015/8/22.
 */
public class ContactsMenu extends LinearLayout {

    public ContactsMenu(Context context) {
        this(context, null);
    }

    public ContactsMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        View rootView = LayoutInflater.from(context).inflate(R.layout.fragment_contact_menu, this);

        ImageView fabContent = new ImageView(context);
        fabContent.setImageDrawable(getResources().getDrawable(R.mipmap.plus));

        FloatingActionButton darkButton = new FloatingActionButton.Builder(context)
                .setTheme(FloatingActionButton.THEME_DARK)
                .setContentView(fabContent)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_CENTER)
                .build();

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(context)
                .setTheme(SubActionButton.THEME_DARK);
        ImageView rlIcon1 = new ImageView(context);
        ImageView rlIcon2 = new ImageView(context);
        ImageView rlIcon3 = new ImageView(context);

        rlIcon1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_action_chat));
        rlIcon2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_action_camera));
        rlIcon3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_action_video));

        // Set 4 SubActionButtons
        com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu centerBottomMenu = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu.Builder(context)
                .setStartAngle(0)
                .setEndAngle(180)
                .setAnimationHandler(new SlideInAnimationHandler())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                .attachTo(darkButton)
                .build();
    }

}
