package com.example.mohammed.withoutname;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import java.util.ArrayList;
import java.util.List;

public class HomeLoginActivity extends AppCompatActivity {
    CircleMenu circleMenu;
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_login);


        circleMenu=(CircleMenu)findViewById(R.id.circle_menu);
        circleMenu.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circleMenu.isOpened()) {
                    circleMenu.closeMenu();
                } else {
                    circleMenu.setVisibility(View.VISIBLE);
                    circleMenu.openMenu();
                }
            }
        });


        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList,circleMenu);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new HomeLoginActivity.GridSpacingItemDecoration(3, dpToPx(7), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();


        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.home, R.drawable.home2);
        circleMenu.addSubMenu(Color.parseColor("#258CFF"), R.drawable.i1)
                .addSubMenu(Color.parseColor("#30A400"), R.drawable.home)
                .addSubMenu(Color.parseColor("#FF4B32"), R.drawable.i3);

        circleMenu.setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {
            @Override
            public void onMenuOpened() {
                recyclerView.setAlpha((float) 0.3);
                floatingActionButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onMenuClosed() {
                recyclerView.setAlpha((float)1.0);
                circleMenu.setVisibility(View.INVISIBLE);
                floatingActionButton.setVisibility(View.VISIBLE);
            }
        });

        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener() {

                                                 @Override
                                                 public void onMenuSelected(int index) {
                                                     switch (index) {
                                                         case 0:
                                                             Handler handler = new Handler();
                                                             handler.postDelayed(new Runnable() {
                                                                 public void run() {
                                                                     DialogMessage();
                                                                 }
                                                             }, 1500);
                                                             break;
                                                         case 1:
                                                             Handler handler1 = new Handler();
                                                             handler1.postDelayed(new Runnable() {
                                                                 public void run() {
                                                                     Intent intent=new Intent(HomeLoginActivity.this,LoginActivity.class);
                                                                     startActivity(intent);
                                                                 }
                                                             }, 1500);
                                                             break;
                                                         case 2:
                                                             Handler handler2 = new Handler();
                                                             handler2.postDelayed(new Runnable() {
                                                                 public void run() {
                                                                     DialogMessage();
                                                                 }
                                                             }, 1500);                        break;
                                                     }
                                                 }

                                             }

        );
    }
    @Override
    public void onBackPressed() {
        if (circleMenu.isOpened()) {
            circleMenu.closeMenu();
        }else
            finish();
    }
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.restaurant,
                R.drawable.coffe,
                R.drawable.bank,
                R.drawable.market,
                R.drawable.mosque,
                R.drawable.atm,
                R.drawable.hospital,
                R.drawable.fuel,
                R.drawable.pharmcy,
                R.drawable.other};

        Album a = new Album("Restaurant", covers[0]);
        albumList.add(a);

        a = new Album("Cafe", covers[1]);
        albumList.add(a);

        a = new Album("Bank",  covers[2]);
        albumList.add(a);

        a = new Album("Market", covers[3]);
        albumList.add(a);

        a = new Album("Mosque", covers[4]);
        albumList.add(a);

        a = new Album("ATM", covers[5]);
        albumList.add(a);

        a = new Album("Hospital & Clinic",  covers[6]);
        albumList.add(a);

        a = new Album("Fuel Station",  covers[7]);
        albumList.add(a);

        a = new Album("Pharmacy",  covers[8]);
        albumList.add(a);

        a = new Album("Other", covers[9]);
        albumList.add(a);

        adapter.notifyDataSetChanged();
    }
    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    private void DialogMessage()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(HomeLoginActivity.this);
        builder.setMessage("You don't login ... Are you want to log in").setTitle("Login Please").setIcon(R.drawable.cast_ic_notification_small_icon)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(HomeLoginActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
}
