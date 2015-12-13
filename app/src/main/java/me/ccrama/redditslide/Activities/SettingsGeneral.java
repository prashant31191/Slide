package me.ccrama.redditslide.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.TimePeriod;

import me.ccrama.redditslide.R;
import me.ccrama.redditslide.Reddit;
import me.ccrama.redditslide.SettingValues;
import me.ccrama.redditslide.Visuals.FontPreferences;


/**
 * Created by ccrama on 3/5/2015.
 */
public class SettingsGeneral extends BaseActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyColorTheme();
        setContentView(R.layout.activity_settings_general);
        setupAppBar(R.id.toolbar, R.string.settings_title_general, true);

        {
            SwitchCompat single = (SwitchCompat) findViewById(R.id.single);

            single.setChecked(!Reddit.single);
            single.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Reddit.single = !isChecked;
                    SettingValues.prefs.edit().putBoolean("Single", !isChecked).apply();

                }
            });
        }
        if (Reddit.expandedSettings) {
            {
                final SeekBar animationMultiplier = (SeekBar) findViewById(R.id.animation_length_sb);
                animationMultiplier.setProgress(Reddit.enter_animation_time_multiplier);
                animationMultiplier.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (progress <= 0) {
                            progress = 1;
                            animationMultiplier.setProgress(1);
                        }
                        SettingValues.prefs.edit().putInt("AnimationLengthMultiplier", progress).apply();
                        Reddit.enter_animation_time_multiplier = progress;
                        Reddit.enter_animation_time = Reddit.enter_animation_time_original * Reddit.enter_animation_time_multiplier;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                final SwitchCompat animation = (SwitchCompat) findViewById(R.id.animation);
                animation.setChecked(Reddit.animation);
                animationMultiplier.setEnabled(Reddit.animation);
                animation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Reddit.animation = isChecked;
                        SettingValues.prefs.edit().putBoolean("Animation", isChecked).apply();
                        animationMultiplier.setEnabled(isChecked);
                    }
                });
            }
        }
        else {
            findViewById(R.id.animation_length_sb).setVisibility(View.GONE);
            findViewById(R.id.enter_animation).setVisibility(View.GONE);
        }
        {
            SwitchCompat single = (SwitchCompat) findViewById(R.id.exitcheck);

            single.setChecked(Reddit.exit);
            single.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Reddit.exit = isChecked;
                    SettingValues.prefs.edit().putBoolean("Exit", isChecked).apply();

                }
            });
        }
        if (Reddit.expandedSettings) {
            {
                SwitchCompat fullscreenswitch = (SwitchCompat) findViewById(R.id.full_screen_images_switch);

                fullscreenswitch.setChecked(Reddit.fullscreen);
                fullscreenswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Reddit.fullscreen = isChecked;
                        SettingValues.prefs.edit().putBoolean("Fullscreen", isChecked).apply();

                    }
                });
            }
        }
        else findViewById(R.id.full_screen_images).setVisibility(View.GONE);
        {


            SwitchCompat nsfw = (SwitchCompat) findViewById(R.id.nsfw);
            final SwitchCompat nsfwprev = (SwitchCompat) findViewById(R.id.nsfwrpev);

            nsfw.setChecked(!SettingValues.NSFWPosts);
            nsfw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SettingValues.prefs.edit().putBoolean("NSFWPostsNew", !isChecked).apply();
                    nsfwprev.setEnabled(!SettingValues.NSFWPosts);
                    SettingValues.NSFWPosts = !isChecked;
                }
            });

            nsfwprev.setEnabled(SettingValues.NSFWPosts);
            nsfwprev.setChecked(!SettingValues.NSFWPreviews);
            nsfwprev.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SettingValues.prefs.edit().putBoolean("NSFWPreviewsNew", !isChecked).apply();
                    SettingValues.NSFWPreviews = !isChecked;

                }
            });

        }
        final TextView color = (TextView) findViewById(R.id.font);
        color.setText(new FontPreferences(this).getFontStyle().getTitle());
        findViewById(R.id.fontsize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(SettingsGeneral.this, v);
                popup.getMenu().add("Large");
                popup.getMenu().add("Medium");
                popup.getMenu().add("Small");

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        new FontPreferences(SettingsGeneral.this).setFontStyle(FontPreferences.FontStyle.valueOf(item.getTitle().toString()));
                        color.setText(new FontPreferences(SettingsGeneral.this).getFontStyle().getTitle());

                        return true;
                    }
                });

                popup.show();
            }
        });
        ((TextView) findViewById(R.id.sorting_current)).setText(Reddit.getSortingStrings(getBaseContext())[Reddit.getSortingId()]);

        {
            findViewById(R.id.sorting).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final DialogInterface.OnClickListener l2 = new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0:
                                    Reddit.defaultSorting = Sorting.HOT;
                                    break;
                                case 1:
                                    Reddit.defaultSorting = Sorting.NEW;
                                    break;
                                case 2:
                                    Reddit.defaultSorting = Sorting.RISING;
                                    break;
                                case 3:
                                    Reddit.defaultSorting = Sorting.TOP;
                                    Reddit.timePeriod = TimePeriod.HOUR;
                                    break;
                                case 4:
                                    Reddit.defaultSorting = Sorting.TOP;
                                    Reddit.timePeriod = TimePeriod.DAY;
                                    break;
                                case 5:
                                    Reddit.defaultSorting = Sorting.TOP;
                                    Reddit.timePeriod = TimePeriod.WEEK;
                                    break;
                                case 6:
                                    Reddit.defaultSorting = Sorting.TOP;
                                    Reddit.timePeriod = TimePeriod.MONTH;
                                    break;
                                case 7:
                                    Reddit.defaultSorting = Sorting.TOP;
                                    Reddit.timePeriod = TimePeriod.YEAR;
                                    break;
                                case 8:
                                    Reddit.defaultSorting = Sorting.TOP;
                                    Reddit.timePeriod = TimePeriod.ALL;
                                    break;
                                case 9:
                                    Reddit.defaultSorting = Sorting.CONTROVERSIAL;
                                    Reddit.timePeriod = TimePeriod.HOUR;
                                    break;
                                case 10:
                                    Reddit.defaultSorting = Sorting.CONTROVERSIAL;
                                    Reddit.timePeriod = TimePeriod.DAY;
                                    break;
                            }
                            SettingValues.prefs.edit().putString("defaultSorting", Reddit.defaultSorting.name()).apply();
                            SettingValues.prefs.edit().putString("timePeriod", Reddit.timePeriod.name()).apply();
                            SettingValues.defaultSorting = Reddit.defaultSorting;
                            SettingValues.timePeriod = Reddit.timePeriod;
                            ((TextView) findViewById(R.id.sorting_current)).setText(
                                    Reddit.getSortingStrings(getBaseContext())[Reddit.getSortingId()]);
                        }
                    };
                    AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(SettingsGeneral.this);
                    builder.setTitle(R.string.sorting_choose);
                    builder.setSingleChoiceItems(
                            Reddit.getSortingStrings(getBaseContext()), Reddit.getSortingId(), l2);
                    builder.show();
                }
            });
        }

    }

}