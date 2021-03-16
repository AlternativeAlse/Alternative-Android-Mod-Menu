package uk.lgl.modmenu;

import android.animation.ArgbEvaluator;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.ALIGN_PARENT_TOP;

public class FloatingModMenuService extends Service {
    public static final String TAG = "Mod_Menu";
    int TEXT_COLOR = Color.parseColor("#FFFFFF");
    int TEXT_COLOR_2 = Color.parseColor("#FFFFFF");
    int MENU_STROKEa = 255;
    int MENU_STROKEr = 255;
    int MENU_STROKEg = 0;
    int MENU_STROKEb = 0;
    GradientDrawable MENU_STROKE = new GradientDrawable();
    int MENU_STROKE_GRADIENT = Color.argb(MENU_STROKEa - 150,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb);
    int MENU_STROKE_COLOR = Color.argb(MENU_STROKEa,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb);
    int BTN_COLOR = Color.parseColor("#1C191F");
    int MENU_BG_COLOR = Color.parseColor("#ff322F42");
    int MENU_FEATURE_BG_COLOR = Color.parseColor("#ff1F1C22");
    GradientDrawable MENU_FEATURE_BG_COLOR_GRADIENT = new GradientDrawable();
    int NOCOLOR = Color.parseColor("#00000000");
    int MENU_WIDTH = 700;
    int MENU_HEIGHT = 400;
    float MENU_CORNER = 4f;
    int ICON_SIZE = 50;
    float ICON_ALPHA = 0.7f;

    int HintTxtColor = Color.parseColor("#FF171E24");

    int ToggleON = Color.parseColor("#138dc2");
    int ToggleOFF = Color.parseColor("#aeaeae");

    int BtnON = Color.parseColor("#322F42");
    int BtnOFF = Color.parseColor("#1C191F");
    int CategoryBG =  Color.parseColor("#391e1f21");
    int SeekBarColor = Color.parseColor("#80CBC4");
    int SeekBarProgressColor = Color.parseColor("#80CBC4");
    int CheckBoxColor = Color.parseColor("#80CBC4");
    int RadioColor =  Color.parseColor("#FFFFFF");
    String fetched = null;
    String NumberTxt = "#41c300";

    GradientDrawable gdMenuBody,gdMenuBody1, gdAnimation = new GradientDrawable();
    RelativeLayout mCollapsed, mRootContainer;
    LinearLayout mExpanded, patches, mSettings, Login;
    LinearLayout.LayoutParams scrlLLExpanded, scrlLL;
    WindowManager mWindowManager;
    WindowManager.LayoutParams params;
    ImageView startimage;
    FrameLayout rootFrame;
    AlertDialog alert;
    EditText edittextvalue;
    ScrollView scrollView;


    TextView inputFieldTextView;
    String inputFieldFeatureName;
    int inputFieldFeatureNum;
    EditTextValue inputFieldTxtValue;

    boolean stopChecking, settingsOpen;

    native String Title();

    native String Heading();

    native String Icon();

    native String IconWebViewData();

    native String[] getFeatureList();

    native String[] settingsList();

    native String[] loginList();

    native boolean isGameLibLoaded();

    GradientDrawable gdMenuStroke = new GradientDrawable();

    GradientDrawable gdMenuStroke1 = new GradientDrawable();

    @Override
    public void onCreate() {
        super.onCreate();
        Preferences.context = this;
        initFloating();
        initAlertDiag();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                Thread();
                handler.postDelayed(this, 1000);
            }
        });
    }
    private void initFloating() {
        rootFrame = new FrameLayout(this);
        rootFrame.setOnTouchListener(onTouchListener());
        mRootContainer = new RelativeLayout(this);
        mCollapsed = new RelativeLayout(this);
        mCollapsed.setVisibility(View.VISIBLE);
        mCollapsed.setAlpha(ICON_ALPHA);
        mExpanded = new LinearLayout(this);
        mExpanded.setVisibility(View.GONE);
        mExpanded.setGravity(Gravity.CENTER);
        mExpanded.setOrientation(LinearLayout.VERTICAL);
        mExpanded.setLayoutParams(new LinearLayout.LayoutParams(MENU_WIDTH, WRAP_CONTENT));
        gdMenuBody = new GradientDrawable();
        gdMenuBody.setCornerRadius(MENU_CORNER);
        gdMenuBody.setColor(MENU_BG_COLOR);

        gdMenuBody1 = new GradientDrawable();
        gdMenuBody1.setCornerRadius(MENU_CORNER);
        gdMenuBody1.setColor(MENU_BG_COLOR);



        MENU_FEATURE_BG_COLOR_GRADIENT.setColor(Color.parseColor("#ff1F1C22"));
        MENU_FEATURE_BG_COLOR_GRADIENT.setCornerRadius(10f);

        MENU_STROKE.setColor(Color.argb(MENU_STROKEa,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb));
        MENU_STROKE.setCornerRadius(10f);

        gdMenuStroke.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        gdMenuStroke.setColor(MENU_BG_COLOR);
        int[] newArray = {MENU_STROKE_GRADIENT,  Color.parseColor("#00000000")};
        gdMenuStroke.setColors(newArray);

        gdMenuStroke1.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        gdMenuStroke1.setColor(MENU_BG_COLOR);
        int[] newArray1 = {MENU_STROKE_GRADIENT,  Color.parseColor("#00000000")};
        gdMenuStroke1.setColors(newArray1);




        startimage = new ImageView(this);
        startimage.setLayoutParams(new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        int applyDimension = (int) TypedValue.applyDimension(1, ICON_SIZE, getResources().getDisplayMetrics());
        startimage.getLayoutParams().height = applyDimension;
        startimage.getLayoutParams().width = applyDimension;
        startimage.requestLayout();
        startimage.setScaleType(ImageView.ScaleType.FIT_XY);
        byte[] decode = Base64.decode(Icon(), 0);
        startimage.setImageBitmap(BitmapFactory.decodeByteArray(decode, 0, decode.length));
        ((ViewGroup.MarginLayoutParams) startimage.getLayoutParams()).topMargin = convertDipToPixels(10);
        startimage.setOnTouchListener(onTouchListener());
        startimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mCollapsed.setVisibility(View.GONE);
                mExpanded.setVisibility(View.VISIBLE);
            }
        });


        WebView wView = new WebView(this);
        wView.loadData("<html>" +
                "<head></head>" +
                "<body style=\"margin: 0; padding: 0\">" +
                "<img src=\"" + IconWebViewData() + "\" width=\"" + ICON_SIZE + "\" height=\"" + ICON_SIZE + "\"" +
                "</body>" +
                "</html>", "text/html", "utf-8");
        wView.setBackgroundColor(0x00000000);
        wView.setAlpha(ICON_ALPHA);
        wView.getSettings().setAppCachePath("/data/data/" + getPackageName() + "/cache");
        wView.getSettings().setAppCacheEnabled(true);
        wView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wView.setOnTouchListener(onTouchListener());
        wView.requestLayout();


        TextView settings = new TextView(this);
        settings.setText(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M ? " info " : " info ");
        settings.setBackground(MENU_FEATURE_BG_COLOR_GRADIENT);
        settings.setTextColor(TEXT_COLOR);
        settings.setTypeface(Typeface.DEFAULT);
        settings.setTextSize(20.0f);
        RelativeLayout.LayoutParams rlsettings = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        rlsettings.addRule(ALIGN_PARENT_RIGHT);
        settings.setLayoutParams(rlsettings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    localChanges(-6, false, 0);
                } catch (IllegalStateException e) {
                }
            }
        });

        mSettings = new LinearLayout(this);
        mSettings.setOrientation(LinearLayout.VERTICAL);
        featureList(settingsList(), mSettings);

        Login = new LinearLayout(this);
        Login.setOrientation(LinearLayout.VERTICAL);
        featureList(loginList(), Login);
        RelativeLayout titleText1 = new RelativeLayout(this);
        titleText1.setVerticalGravity(16);
        RelativeLayout titleText = new RelativeLayout(this);
        titleText.setPadding(0, 20, 30,  20);
        titleText.setVerticalGravity(16);
        titleText.setBackground(gdMenuBody);

        TextView title2 = new TextView(this);
        title2.setText("                                                                                         ");
        title2.setBackground(MENU_STROKE);
        title2.setTextColor(TEXT_COLOR);
        title2.setTextSize(3.0f);
        title2.setGravity(Gravity.TOP);
        RelativeLayout.LayoutParams rl2 = new RelativeLayout.LayoutParams(MENU_WIDTH, WRAP_CONTENT);
        rl2.addRule(RelativeLayout.FOCUS_LEFT);
        title2.setLayoutParams(rl2);

        TextView title4 = new TextView(this);
        title4.setText("                                                                                         ");
        title4.setBackground(gdMenuStroke1);
        title4.setTextColor(TEXT_COLOR);
        title4.setTextSize(10.0f);
        title4.setGravity(Gravity.TOP);
        RelativeLayout.LayoutParams rl3 = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        rl3.addRule(RelativeLayout.FOCUS_LEFT);
        title4.setPadding(1000, 0, 1000, 0);
        title4.setLayoutParams(rl3);

        TextView title = new TextView(this);
        title.setPadding(20, 0, 20, 0);
        title.setText(Html.fromHtml(Title()));
        title.setTextColor(TEXT_COLOR);
        title.setTextSize(18.0f);
        title.setGravity(Gravity.LEFT);
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        rl.addRule(RelativeLayout.FOCUS_LEFT);
        title.setLayoutParams(rl);



        TextView heading = new TextView(this);
        heading.setText(Html.fromHtml(Heading()));
        heading.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        heading.setMarqueeRepeatLimit(-1);
        heading.setSingleLine(true);
        heading.setSelected(true);
        heading.setTextColor(TEXT_COLOR);
        heading.setTextSize(10.0f);
        heading.setGravity(Gravity.CENTER);
        heading.setPadding(0, 0, 0, 5);


        scrollView = new ScrollView(this);
        scrlLL = new LinearLayout.LayoutParams(MATCH_PARENT, MENU_HEIGHT);
        scrlLLExpanded = new LinearLayout.LayoutParams(mExpanded.getLayoutParams());
        scrlLLExpanded.weight = 1.0f;
        scrollView.setLayoutParams(Preferences.loadPrefBoolean("Auto size vertically", -2) ? scrlLLExpanded : scrlLL);
        scrollView.setBackgroundColor(MENU_FEATURE_BG_COLOR);
        patches = new LinearLayout(this);
        patches.setOrientation(LinearLayout.VERTICAL);
        patches.setBackgroundColor(NOCOLOR);


        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setPadding(0, -20, 0, -20);
        relativeLayout.setVerticalGravity(Gravity.CENTER);
        relativeLayout.setBackground(gdMenuBody1);

        TextView bg1 = new TextView(this);
        bg1.setText("                                                                                         ");
        bg1.setBackgroundColor(MENU_BG_COLOR);
        bg1.setTextColor(TEXT_COLOR);
        bg1.setTextSize(3.0f);
        bg1.setGravity(Gravity.TOP);
        RelativeLayout.LayoutParams rl5 = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        rl5.addRule(RelativeLayout.FOCUS_LEFT);
        bg1.setPadding(-100, 10, 1000, 10);
        bg1.setLayoutParams(rl5);

        Button hideBtn = new Button(this);
        hideBtn.setBackgroundColor(Color.TRANSPARENT);
        hideBtn.setText("Dev AlvinHacker, IseStudio");
        hideBtn.setTextColor(TEXT_COLOR);
        hideBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mCollapsed.setVisibility(View.VISIBLE);
                mCollapsed.setAlpha(0);
                mExpanded.setVisibility(View.GONE);

            }
        });
        hideBtn.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {

                FloatingModMenuService.this.stopSelf();
                return false;
            }
        });

        Button closeBtn = new Button(this);
        closeBtn.setBackgroundColor(Color.TRANSPARENT);
        closeBtn.setText("By Alvin % Ise   ");
        closeBtn.setTextColor(TEXT_COLOR);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mCollapsed.setVisibility(View.VISIBLE);
                mCollapsed.setAlpha(ICON_ALPHA);
                mExpanded.setVisibility(View.GONE);
            }
        });

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        closeBtn.setLayoutParams(layoutParams);


        int iparams = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ? 2038 : 2002;
        params = new WindowManager.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, iparams, 8, -3);
        params.gravity = 51;
        params.gravity = 51;
        params.x = 0;
        params.y = 100;


        rootFrame.addView(mRootContainer);
        mRootContainer.addView(mCollapsed);
        mRootContainer.addView(mExpanded);
        if (IconWebViewData() != null) {
            mCollapsed.addView(wView);
        } else {
            mCollapsed.addView(startimage);
        }
        titleText1.addView(titleText);
        titleText.addView(title);
        titleText1.addView(title2);
        titleText1.addView(title4);
        titleText.addView(settings);
        mExpanded.addView(titleText1);
        mExpanded.addView(heading);
        scrollView.addView(Login);
        mExpanded.addView(scrollView);
        relativeLayout.addView(bg1);
        relativeLayout.addView(hideBtn);
        relativeLayout.addView(closeBtn);
        mExpanded.addView(relativeLayout);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(rootFrame, params);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            boolean viewLoaded = false;

            @Override
            public void run() {
                if (Preferences.savePref && !isGameLibLoaded() && !stopChecking) {
                    if (!viewLoaded) {
                        patches.addView(Category("Save preferences was been enabled. Waiting for game lib to be loaded...\n\nForce load menu may not apply mods instantly. You would need to reactivate them again"));
                        patches.addView(Button(-100, "Force load menu"));
                        viewLoaded = true;
                    }
                    handler.postDelayed(this, 600);
                } else {
                    patches.removeAllViews();
                    featureList(getFeatureList(), patches);
                }
            }
        }, 500);
    }

    private void featureList(String[] listFT, LinearLayout linearLayout) {
        for (int i = 0; i < listFT.length; i++) {
            String str = listFT[i];
            String[] strSplit = str.split("_");
            if (strSplit[1].equals("Toggle")) {
                linearLayout.addView(Switch(Integer.parseInt(strSplit[0]), strSplit[2]));
            } else if (strSplit[1].equals("SeekBar")) {
                linearLayout.addView(SeekBar(Integer.parseInt(strSplit[0]), strSplit[2], Integer.parseInt(strSplit[3]), Integer.parseInt(strSplit[4])));
            } else if (strSplit[1].equals("Button")) {
                linearLayout.addView(Button(Integer.parseInt(strSplit[0]), strSplit[2]));
            } else if (strSplit[1].equals("ButtonLink")) {
                linearLayout.addView(ButtonLink(strSplit[2], strSplit[3]));
            } else if (strSplit[1].equals("ButtonOnOff")) {
                linearLayout.addView(ButtonOnOff(Integer.parseInt(strSplit[0]), strSplit[2]));
            } else if (strSplit[1].equals("Spinner")) {
                linearLayout.addView(RichTextView(strSplit[2]));
                linearLayout.addView(Spinner(Integer.parseInt(strSplit[0]), strSplit[2], strSplit[3]));
            } else if (strSplit[1].equals("InputValue")) {
                linearLayout.addView(TextField(Integer.parseInt(strSplit[0]), strSplit[2]));
            } else if (strSplit[1].equals("CheckBox")) {
                linearLayout.addView(CheckBox(Integer.parseInt(strSplit[0]), strSplit[2]));
            } else if (strSplit[1].equals("Category")) {
                linearLayout.addView(Category(strSplit[2]));
            } else if (strSplit[1].equals("RichTextView")) {
                linearLayout.addView(RichTextView(strSplit[2]));
            } else if (strSplit[1].equals("RichWebView")) {
                linearLayout.addView(RichWebView(strSplit[2]));
            } else if (strSplit[1].equals("RadioButton")) {
                linearLayout.addView(RadioButton(Integer.parseInt(strSplit[0]), strSplit[2], strSplit[3]));
            }
        }
    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {
            final View collapsedView = mCollapsed;
            final View expandedView = mExpanded;
            private float initialTouchX, initialTouchY;
            private int initialX, initialY;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int rawX = (int) (motionEvent.getRawX() - initialTouchX);
                        int rawY = (int) (motionEvent.getRawY() - initialTouchY);


                        if (rawX < 10 && rawY < 10 && isViewCollapsed()) {
                            try {
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            } catch (NullPointerException e) {

                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:

                        params.x = initialX + ((int) (motionEvent.getRawX() - initialTouchX));
                        params.y = initialY + ((int) (motionEvent.getRawY() - initialTouchY));

                        mWindowManager.updateViewLayout(rootFrame, params);
                        return true;
                    default:
                        return false;
                }
            }
        };
    }


    private void initAlertDiag() {

        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setPadding(5, 5, 5, 5);
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        linearLayout1.setBackgroundColor(MENU_FEATURE_BG_COLOR);

        LinearLayout linearLayout5 = new LinearLayout(this);
        linearLayout5.setOrientation(LinearLayout.VERTICAL);


        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.addView(linearLayout5);


        final TextView textView = new TextView(this);
        textView.setText("Tap OK to apply changes. Tap outside to cancel");
        textView.setTextColor(TEXT_COLOR_2);


        edittextvalue = new EditText(this);
        edittextvalue.setMaxLines(1);
        edittextvalue.setWidth(convertDipToPixels(300));
        edittextvalue.setTextColor(TEXT_COLOR_2);
        edittextvalue.setHintTextColor(HintTxtColor);
        edittextvalue.setInputType(InputType.TYPE_CLASS_NUMBER);
        edittextvalue.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(10);
        edittextvalue.setFilters(FilterArray);


        Button button = new Button(this);
        button.setBackgroundColor(BTN_COLOR);
        button.setTextColor(TEXT_COLOR_2);
        button.setText("OK");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputFieldTxtValue.setValue(Integer.parseInt(edittextvalue.getText().toString()));
                inputFieldTextView.setText(Html.fromHtml("<font face='roboto'>" + inputFieldFeatureName + ": <font color='#41c300'>" + edittextvalue.getText().toString() + "</font></font>"));
                alert.dismiss();
                localChanges(inputFieldFeatureNum, false, Integer.parseInt(edittextvalue.getText().toString()));
                Preferences.changeFeatureInt(inputFieldFeatureName, inputFieldFeatureNum, Integer.parseInt(edittextvalue.getText().toString()));
            }
        });

        alert = new AlertDialog.Builder(this, 2).create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alert.getWindow()).setType(Build.VERSION.SDK_INT >= 26 ? 2038 : 2002);
        }
        linearLayout1.addView(textView);
        linearLayout1.addView(edittextvalue);
        linearLayout1.addView(button);
        alert.setView(linearLayout1);
    }

    private View Switch(final int featureNum, final String featureName) {
        final Switch switchR = new Switch(this);
        ColorStateList buttonStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        Color.BLUE,
                        ToggleON,
                        ToggleOFF
                }
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switchR.getThumbDrawable().setTintList(buttonStates);
            switchR.getTrackDrawable().setTintList(buttonStates);
        }

        switchR.setText(featureName);
        switchR.setTextColor(TEXT_COLOR_2);
        switchR.setPadding(10, 5, 0, 5);
        switchR.setChecked(Preferences.loadPrefBoolean(featureName, featureNum));
        switchR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                localChanges(featureNum, isChecked, 0);
                Preferences.changeFeatureBoolean(featureName, featureNum, isChecked);
            }
        });
        return switchR;
    }

    private View SeekBar(final int featureNum, final String featureName, final int min, int max) {
        int loadedProg = Preferences.loadPrefInt(featureName, featureNum);
        LinearLayout linearLayout6 = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        linearLayout6.setPadding(10, 5, 0, 5);
        RelativeLayout relativeLayout5 = new RelativeLayout(this);
        relativeLayout5.setPadding(10, 5, 10, 5);
        relativeLayout5.setVerticalGravity(16);
        relativeLayout5.setBackgroundColor(MENU_FEATURE_BG_COLOR);


        final TextView textView = new TextView(this);
        textView.setText(Html.fromHtml("<font face='roboto'>" + featureName + " <font color='" + TEXT_COLOR_2 +"'>" + ((loadedProg == 0) ? min : loadedProg) + "</font>"));
        textView.setTextColor(TEXT_COLOR_2);


        SeekBar seekBar = new SeekBar(this);
        seekBar.setLayoutParams(layoutParams);
        seekBar.setPadding(300, 0, 0, 0);
        seekBar.setMax(max);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            seekBar.setMin(min);
        seekBar.setProgress((loadedProg == 0) ? min : loadedProg);

        seekBar.getThumb().setAlpha(0);
        seekBar.getProgressDrawable().setColorFilter(MENU_STROKE_COLOR, PorterDuff.Mode.SRC_ATOP);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.getProgressDrawable().setColorFilter(MENU_STROKE_COLOR, PorterDuff.Mode.SRC_ATOP);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.getProgressDrawable().setColorFilter(MENU_STROKE_COLOR, PorterDuff.Mode.SRC_ATOP);
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                seekBar.getProgressDrawable().setColorFilter(MENU_STROKE_COLOR, PorterDuff.Mode.SRC_ATOP);

                seekBar.setProgress(i < min ? min : i);
                localChanges(featureNum, false, i < min ? min : i);
                Preferences.changeFeatureInt(featureName, featureNum, i < min ? min : i);
                textView.setText(Html.fromHtml("<font face='roboto'>" + featureName + " <font color='" + TEXT_COLOR_2 +"'>" + (i < min ? min : i) + "</font>"));
            }
        });
        relativeLayout5.addView(textView);
        relativeLayout5.addView(seekBar);

        return relativeLayout5;
    }

    private View Button(final int featureNum, final String featureName) {
        final Button button = new Button(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, 47);
        layoutParams.setMargins(0, 1, 0, 1);
        button.setLayoutParams(layoutParams);

        button.setTextColor(TEXT_COLOR_2);
        button.setGravity(Gravity.LEFT);
        button.setAllCaps(false);
        button.setText(Html.fromHtml(featureName));
        button.setBackgroundColor(BTN_COLOR);
        layoutParams.setMargins(0, 1, 0, 1);
        button.setLayoutParams(layoutParams);
        button.setTextColor(TEXT_COLOR_2);
        button.setAllCaps(false);
        button.setPadding(30,10,0,0);
        button.setGravity(Gravity.LEFT);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                localChanges(featureNum, false, 0);
                Preferences.changeFeatureInt(featureName, featureNum, 0);
            }
        });

        return button;
    }

    private View ButtonLink(final String featureName, final String url) {
        final Button button = new Button(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, 47);
        layoutParams.setMargins(7, 5, 7, 5);
        button.setLayoutParams(layoutParams);
        button.setAllCaps(false);
        button.setTextColor(TEXT_COLOR_2);
        button.setGravity(Gravity.CENTER);
        button.setText(featureName);
        button.setBackgroundColor(BTN_COLOR);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        return button;
    }

    private View ButtonOnOff(final int featureNum, String featureName) {
        final Button button = new Button(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, 47);
        layoutParams.setMargins(0, 1, 0, 1);
        button.setLayoutParams(layoutParams);
        button.setTextColor(TEXT_COLOR_2);
        button.setAllCaps(false);
        button.setPadding(30,10,0,0);
        button.setGravity(Gravity.LEFT);

        final String finalFeatureName = featureName.replace("OnOff_", "");
        boolean isOn = Preferences.loadPrefBoolean(featureName, featureNum);
        if (isOn) {
            button.setText(finalFeatureName);
            button.setBackgroundColor(BtnON);
            isOn = false;
        } else {
            button.setText(finalFeatureName);
            button.setBackgroundColor(BtnOFF);
            isOn = true;
        }
        final boolean finalIsOn = isOn;
        button.setOnClickListener(new View.OnClickListener() {
            boolean isOn = finalIsOn;

            public void onClick(View v) {
                localChanges(featureNum, isOn, 0);
                Preferences.changeFeatureBoolean(finalFeatureName, featureNum, isOn);

                if (isOn) {
                    button.setText(finalFeatureName);
                    button.setBackgroundColor(BtnON);
                    isOn = false;
                } else {
                    button.setText(finalFeatureName);
                    button.setBackgroundColor(BtnOFF);
                    isOn = true;
                }
            }
        });

        return button;
    }

    private View Spinner(final int featureNum, final String featureName, final String list) {
        final List<String> lists = new LinkedList<>(Arrays.asList(list.split(",")));

        LinearLayout linearLayout2 = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        layoutParams2.setMargins(10, 2, 10, 5);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        linearLayout2.setBackgroundColor(BTN_COLOR);
        linearLayout2.setLayoutParams(layoutParams2);

        final Spinner spinner = new Spinner(this, Spinner.MODE_DROPDOWN);
        spinner.setPadding(5, 10, 5, 8);
        spinner.setLayoutParams(layoutParams2);
        spinner.getBackground().setColorFilter(1, PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lists);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(aa);
        spinner.setSelection(Preferences.loadPrefInt(featureName, featureNum));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Preferences.changeFeatureInt(spinner.getSelectedItem().toString(), featureNum, position);
                ((TextView) parentView.getChildAt(0)).setTextColor(TEXT_COLOR_2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        linearLayout2.addView(spinner);
        return linearLayout2;
    }

    private View TextField(final int feature, final String featureName) {
        RelativeLayout relativeLayout2 = new RelativeLayout(this);
        relativeLayout2.setPadding(10, 5, 10, 5);
        relativeLayout2.setVerticalGravity(16);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.topMargin = 15;

        final TextView textView = new TextView(this);
        int num = Preferences.loadPrefInt(featureName, feature);
        textView.setText(Html.fromHtml("<font face='roboto'>" + featureName + ": <font color='" + NumberTxt +"'>" + num + "</font></font>"));
        textView.setTextColor(TEXT_COLOR_2);
        textView.setLayoutParams(layoutParams);

        final EditTextValue edittextval = new EditTextValue();

        localChanges(2, false, num);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams2.addRule(ALIGN_PARENT_RIGHT);

        Button button2 = new Button(this);
        button2.setLayoutParams(layoutParams2);
        button2.setBackgroundColor(BTN_COLOR);
        button2.setText("SET");
        button2.setTextColor(TEXT_COLOR_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.show();
                inputFieldTextView = textView;
                inputFieldFeatureNum = feature;
                inputFieldFeatureName = featureName;
                inputFieldTxtValue = edittextval;
                edittextvalue.setText(String.valueOf(edittextval.getValue()));
            }
        });

        relativeLayout2.addView(textView);
        relativeLayout2.addView(button2);
        return relativeLayout2;
    }

    private View CheckBox(final int featureNum, final String featureName) {
        final CheckBox checkBox = new CheckBox(this);
        checkBox.setText(featureName);
        checkBox.setTextColor(TEXT_COLOR_2);
        checkBox.setChecked(Preferences.loadPrefBoolean(featureName, featureNum));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    Preferences.changeFeatureBoolean(featureName, featureNum, isChecked);
                } else {
                    Preferences.changeFeatureBoolean(featureName, featureNum, isChecked);
                }
            }
        });
        return checkBox;
    }

    private View RadioButton(final int featureNum, String featureName, final String list) {

        final List<String> lists = new LinkedList<>(Arrays.asList(list.split(",")));

        final TextView textView = new TextView(this);
        textView.setText(featureName + ":");
        textView.setTextColor(TEXT_COLOR_2);

        final RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setPadding(10, 5, 10, 5);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.addView(textView);

        for (int i = 0; i < lists.size(); i++) {
            final RadioButton Radioo = new RadioButton(this);
            final String finalFeatureName = featureName, radioName = lists.get(i);
            View.OnClickListener first_radio_listener = new View.OnClickListener() {
                public void onClick(View v) {
                    textView.setText(finalFeatureName + ": " + radioName);
                    textView.setText(Html.fromHtml("<font face='roboto'>" + finalFeatureName + ": <font color='" + NumberTxt +"'>" + radioName + "</font>"));
                    Preferences.changeFeatureInt(finalFeatureName, featureNum, radioGroup.indexOfChild(Radioo));
                }
            };
            System.out.println(lists.get(i));
            Radioo.setText(lists.get(i));
            Radioo.setTextColor(Color.LTGRAY);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                Radioo.setButtonTintList(ColorStateList.valueOf(RadioColor));
            Radioo.setOnClickListener(first_radio_listener);
            radioGroup.addView(Radioo);
        }

        return radioGroup;
    }

    private View Category(String text) {
        LinearLayout linearLayout7 = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        linearLayout7.setOrientation(LinearLayout.VERTICAL);

        TextView title3 = new TextView(this);
        title3.setText("                                                                                         ");
        title3.setBackground(MENU_STROKE);
        title3.setTextColor(TEXT_COLOR);
        title3.setTextSize(1.0f);
        title3.setGravity(Gravity.TOP);
        title3.setPadding(1000, 0, 1000, 0);

        TextView textView = new TextView(this);
        textView.setBackground(gdMenuStroke);
        textView.setText(Html.fromHtml(text));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(14.0f);
        textView.setTextColor(TEXT_COLOR_2);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(0, 5, 0, 5);

        linearLayout7.addView(title3);
        linearLayout7.addView(textView);
        return linearLayout7;
    }

    private View RichTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(Html.fromHtml(text));
        textView.setTextColor(TEXT_COLOR_2);
        textView.setPadding(10, 5, 10, 5);
        return textView;
    }

    private View RichWebView(String text) {
        WebView wView = new WebView(this);
        wView.loadData(text, "text/html", "utf-8");
        wView.setBackgroundColor(0x00000000);
        wView.setPadding(0, 5, 0, 5);
        wView.getSettings().setAppCacheEnabled(false);
        wView.requestLayout();
        return wView;
    }

    public static String hsvToRgb(float hue, float saturation, float value) {

        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0: return rgbToString(value, t, p);
            case 1: return rgbToString(q, value, p);
            case 2: return rgbToString(p, value, t);
            case 3: return rgbToString(p, q, value);
            case 4: return rgbToString(t, p, value);
            case 5: return rgbToString(value, p, q);
            default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
    }

    public static String rgbToString(float r, float g, float b) {
        String rs = Integer.toHexString((int)(r * 256));
        String gs = Integer.toHexString((int)(g * 256));
        String bs = Integer.toHexString((int)(b * 256));
        return rs + gs + bs;
    }

    private void localChanges(int featureNum, boolean toggle, int value) {
        URL url;
        switch (featureNum) {
            case -1:

                break;
            case -2:
                scrollView.setLayoutParams(toggle ? scrlLLExpanded : scrlLL);
                break;
            case -4:
                Logcat.Save(getApplicationContext());
                break;
            case -5:
                Logcat.Clear(getApplicationContext());
                break;
            case -6:
                settingsOpen = !settingsOpen;
                if (settingsOpen){
                    scrollView.removeView(patches);
                    scrollView.addView(mSettings);
                }
                else {
                    scrollView.removeView(mSettings);
                    scrollView.addView(patches);
                }
                break;
            case -100:
                stopChecking = true;
                break;
            case 2:
                if (value == 228) {
                    scrollView.removeView(Login);
                    scrollView.addView(patches);
                }
                break;
            case -10:
                int colorr = value;
                if (colorr >= 255) {
                    colorr = 255;
                }
                MENU_STROKEr = colorr;
                MENU_STROKE_GRADIENT = Color.argb(MENU_STROKEa - 150,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb);
                MENU_STROKE_COLOR = Color.argb(MENU_STROKEa,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb);
                MENU_FEATURE_BG_COLOR_GRADIENT.setColor(Color.parseColor("#ff1F1C22"));
                MENU_FEATURE_BG_COLOR_GRADIENT.setCornerRadius(10f);

                MENU_STROKE.setColor(Color.argb(MENU_STROKEa,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb));
                MENU_STROKE.setCornerRadius(10f);

                gdMenuStroke.setColor(MENU_BG_COLOR);
                int[] newArray723 = {MENU_STROKE_GRADIENT,  Color.parseColor("#00000000")};
                gdMenuStroke.setColors(newArray723);

                gdMenuStroke1.setColor(MENU_BG_COLOR);
                int[] newArray16 = {MENU_STROKE_GRADIENT,  Color.parseColor("#00000000")};
                gdMenuStroke1.setColors(newArray16);
                break;
            case -11:
                int colorg = value;
                if (colorg >= 255) {
                    colorg = 255;
                }
                MENU_STROKEg = colorg;
                MENU_STROKE.setColor(Color.argb(MENU_STROKEa,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb));
                MENU_STROKE.setCornerRadius(10f);
                MENU_STROKE_GRADIENT = Color.argb(MENU_STROKEa - 150,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb);
                MENU_STROKE_COLOR = Color.argb(MENU_STROKEa,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb);

                MENU_STROKE.setColor(Color.argb(MENU_STROKEa,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb));
                MENU_STROKE.setCornerRadius(10f);

                gdMenuStroke.setColor(MENU_BG_COLOR);
                int[] newArray1 = {MENU_STROKE_GRADIENT,  Color.parseColor("#00000000")};
                gdMenuStroke.setColors(newArray1);

                gdMenuStroke1.setColor(MENU_BG_COLOR);
                int[] newArray14 = {MENU_STROKE_GRADIENT,  Color.parseColor("#00000000")};
                gdMenuStroke1.setColors(newArray14);
                break;
            case -12:
                int colorb = value;
                if (colorb >= 255) {
                    colorb = 255;
                }
                MENU_STROKEb = colorb;
                MENU_STROKE.setColor(Color.argb(MENU_STROKEa,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb));
                MENU_STROKE.setCornerRadius(10f);
                MENU_STROKE_GRADIENT = Color.argb(MENU_STROKEa - 150,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb);
                MENU_STROKE_COLOR = Color.argb(MENU_STROKEa,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb);

                MENU_STROKE.setColor(Color.argb(MENU_STROKEa,MENU_STROKEr,MENU_STROKEg,MENU_STROKEb));
                MENU_STROKE.setCornerRadius(10f);

                gdMenuStroke.setColor(MENU_BG_COLOR);
                int[] newArray12 = {MENU_STROKE_GRADIENT,  Color.parseColor("#00000000")};
                gdMenuStroke.setColors(newArray12);

                gdMenuStroke1.setColor(MENU_BG_COLOR);
                int[] newArray13 = {MENU_STROKE_GRADIENT,  Color.parseColor("#00000000")};
                gdMenuStroke1.setColors(newArray13);
                break;
            case -13:
                MENU_WIDTH = value;
                break;
            case -14:
                MENU_HEIGHT = value;
                break;
        }
    }



    public int onStartCommand(Intent intent, int i, int i2) {
        return Service.START_NOT_STICKY;
    }

    private boolean isViewCollapsed() {
        return rootFrame == null || mCollapsed.getVisibility() == View.VISIBLE;
    }


    private int convertDipToPixels(int i) {
        return (int) ((((float) i) * getResources().getDisplayMetrics().density) + 0.5f);
    }

    private int dp(int i) {
        return (int) TypedValue.applyDimension(1, (float) i, getResources().getDisplayMetrics());
    }


    private boolean isNotInGame() {
        RunningAppProcessInfo runningAppProcessInfo = new RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(runningAppProcessInfo);
        return runningAppProcessInfo.importance != 100;
    }


    public void onDestroy() {
        super.onDestroy();
        if (rootFrame != null) {
            mWindowManager.removeView(rootFrame);
        }
        stopSelf();
    }

    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
        stopSelf();
    }

    private void Thread() {
        if (rootFrame == null) {
            return;
        }
        if (isNotInGame()) {
            rootFrame.setVisibility(View.INVISIBLE);
        } else {
            rootFrame.setVisibility(View.VISIBLE);
        }
    }

    private class EditTextValue {
        private int val;

        public void setValue(int i) {
            val = i;
        }

        public int getValue() {
            return val;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}