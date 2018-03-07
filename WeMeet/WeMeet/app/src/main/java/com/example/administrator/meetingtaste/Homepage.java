package com.example.administrator.meetingtaste;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.meetingtaste.model.Artical;
import com.example.administrator.meetingtaste.model.ArticalAndUser;
import com.example.administrator.meetingtaste.model.Items;
import com.example.administrator.meetingtaste.model.ShoppingCar;
import com.example.administrator.meetingtaste.model.ShoppingcarAndItem;
import com.example.administrator.meetingtaste.model.User;
import com.example.administrator.meetingtaste.service.DBFactory;
import com.example.administrator.meetingtaste.service.DBService;
import com.github.mzule.fantasyslide.SideBar;
import com.github.mzule.fantasyslide.SimpleFantasyListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE;
import static java.lang.Boolean.FALSE;

/**
 * 主页，5个栏
 */
public class Homepage extends AppCompatActivity {
    // 传来的登录用户信息
    private final User loginUser = new User();

    private static final String BREAKFAST_TAG="breakfast";
    private static final String LUNCH_TAG="lunch";
    private static final String DINNER_TAG="dinner";
    private static final String MIDNIGHT_SNACK_TAG="midnight_snack";
    private static final String HAOWU_TAG="haowu";
    private static final String KETANG_TAG="ketang";
    private static final String SHOPPINGCAR_TAG="shoppingcar";
    private static final String LOGIN_USER="LOGIN_USER";//跳转的时候传输用户ID的key
    private static final String ARTICAL="ARTICAL";//跳转的时候传输对应文章的key
    private static final String DIANPU_ITEM="DIANPU_ITEM";//跳转到店铺物品评论的时候的key
    private static final int DIANPU_ITEM_REQUEST_CODE=123;
    private static final int REQUEST_EDIT=321;
    private static final int RESULT_EDIT=666;

    private TextView mTextMessage;
    private DrawerLayout drawerLayout;

    private View haowu_view, gouwu_view, wode_view, dianpu_view, ketang_view;
    private List<View> pageList;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private SideBar viewSide;
    private LayoutInflater inflater;

    private RecyclerView haowuRecyclerView;
    private CommonAdapter<Items> haowuAdapter;
    private SwipeRefreshLayout haowuSwipeRefreshLayout;
    private List<Items> haowuList;

    private RecyclerView ketangRecyclerView;
    private CommonAdapter<ArticalAndUser> ketangAdapter;
    private List<ArticalAndUser> ketangList;
    private SwipeRefreshLayout ketangSwipeRefreshLayout;
    private ImageView bestContentView;
    private SwipeRefreshLayout dianpuswipeRefreshLayout;
    private RecyclerView dianpuRecyclerView;
    private CommonAdapter<Items> dianpuAdapter;
    private List<Items> dianpuList;
    private Button breakfast;
    private Button lunch;
    private Button dinner;
    private Button midnight_snack;


    private static int dianpuPage=8;//显示的子项数目
    private static Boolean isLoadingMore=false; // true--没有更多了
    private static Boolean isLoading=false; //true--正在加载

    private RecyclerView gouwuRecyclerView;
    private CommonAdapter<ShoppingcarAndItem> gouwuAdapter;
    private Button payForIt;

    private RecyclerView wodeRecyclerView;
    private CommonAdapter<ArticalAndUser> wodeAdapter;
    List<ArticalAndUser> wodeList;
    private SwipeRefreshLayout wodeRefreshLayout;

    private BigDecimal total_price = new BigDecimal("0");

    private BottomNavigationViewEx.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationViewEx.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ImageLoader.getInstance().clearMemoryCache();
            ImageLoader.getInstance().clearDiskCache();
            switch (item.getItemId()) {
                case R.id.navigation_haowu:
                    setHaowuRecyclerView();
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_ketang:
                    setKetangRecyclerView();
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_dianpu:
                    setDianpuRecyclerView();
//                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_gouwu:
                    setGouwuRecyclerView();
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_wode:
                    initUserData();
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };
    private ServiceConnection sc;
    private IBinder mBinder;

    private void bind_service() {
        Intent intent = new Intent(this, MusicServer.class);
        startService(intent);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Permission.verifyStoragePermissions(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        setHaowuRecyclerView();

        final DrawerArrowDrawable indicator = new DrawerArrowDrawable(this);
        indicator.setColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(indicator);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        // 禁止左右滑动手势
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (((ViewGroup) drawerView).getChildAt(1).getId() == R.id.leftSideBar) {
                    indicator.setProgress(slideOffset);
                    // 侧边栏信息
                    viewSide = (SideBar) findViewById(R.id.leftSideBar);
                    String name = loginUser.getUser_name();
                    String iconUrl = loginUser.getUser_icon();
                    TextView userName = (TextView) viewSide.findViewById(R.id.userName);
                    userName.setText(name);
                    CircleImageView userIcon = (CircleImageView) viewSide.findViewById(R.id.userIcon);
                    ImageLoader.getInstance().displayImage(iconUrl, userIcon);
                }
            }
        });

        // 启动bgm
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                Log.d("service", "connected");
                mBinder = service;
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBinder = null;
                sc = null;
            }
        };
        //bind_service();
    }
    private void init() {
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };
        int[] colors = new int[]{ContextCompat.getColor(this, R.color.colorGray),
                ContextCompat.getColor(this, R.color.secondColor)};
//        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationViewEx navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        navigation.enableAnimation(true);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        ColorStateList csl = new ColorStateList(states, colors);
        navigation.setItemTextColor(csl);
        navigation.setItemIconTintList(csl);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        inflater = getLayoutInflater().from(this);
        haowu_view = inflater.inflate(R.layout.home_page_haowu, null);
        dianpu_view = inflater.inflate(R.layout.home_page_dianpu, null);
        wode_view = inflater.inflate(R.layout.home_page_wode, null);
        ketang_view = inflater.inflate(R.layout.home_page_ketang, null);
        gouwu_view = inflater.inflate(R.layout.home_page_gouwu, null);
        pageList = new ArrayList<>();
        pageList.add(haowu_view);
        pageList.add(ketang_view);
        pageList.add(dianpu_view);
        pageList.add(gouwu_view);
        pageList.add(wode_view);
        viewPagerAdapter = new ViewPagerAdapter(pageList);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
//        viewPager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        navigation.setupWithViewPager(viewPager);
        initUserData();

//        Intent intent = getIntent();
//        User user = (User) intent.getSerializableExtra("LOGIN_USER");
//        loginUser.setUser(user);
    }

    // 侧边栏动画
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return true;
    }

    // 侧边栏跳转
    public void onClick(final View view) {
        Bundle mBundle = getIntent().getExtras();
        DBService dbService = DBFactory.createDBService();
        dbService.queryUserById(((User)mBundle.get(LOGIN_USER)).getUser_id())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        if (view instanceof TextView) {
                            String title = ((TextView) view).getText().toString();
                            if(title.equals("      城市")) {
                                String city = loginUser.getUser_city();
                                if(city != null)
                                    Toast.makeText(Homepage.this, "我在".concat(city).concat("等你"), Toast.LENGTH_SHORT).show();
                            }
                            else if(title.equals("      钱包")) {
                                String price = String.valueOf(loginUser.getUser_account());
                                if(loginUser.getUser_account() != null)
                                    Toast.makeText(Homepage.this, "嘤嘤嘤 还剩 ¥".concat(price).concat(" 口粮"), Toast.LENGTH_SHORT).show();
                            }
                            else if(title.equals("      文章")) {
                                String artical = String.valueOf(loginUser.getUser_artical_count());
                                Toast.makeText(Homepage.this, "共有".concat(artical).concat("篇种草日记"), Toast.LENGTH_SHORT).show();
                            }
                            else if(title.equals("  注销登录")) {
                                // 返回登录界面
                                startActivity(new Intent(Homepage.this, Login.class));
                            }
                        } else if (view.getId() == R.id.userInfo) {
                            String name = loginUser.getUser_name();
                            String iconUrl = loginUser.getUser_icon();
                            TextView userName = (TextView) view.findViewById(R.id.userName);
                            userName.setText(name);
                            CircleImageView userIcon = (CircleImageView) view.findViewById(R.id.userIcon);
                            ImageLoader.getInstance().displayImage(iconUrl, userIcon);
                            Toast.makeText(Homepage.this, name.concat(", 欢迎你呀"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(User user) {
                        loginUser.setUser(user);
                    }
                });

    }

    /**
     *
     * @param begin 从第几个开始加载
     * @param flag true是上拉刷新，false是下拉加载
     */
    private void loadHaowuItem(int begin,final Boolean flag)
    {
        if(!isLoading) //如果不在加载
        {
            isLoading=true; //设置成正在加载
            /*异步加载*/
            DBService dbService= DBFactory.createDBService();
            dbService.queryItemByTag(HAOWU_TAG,begin,8) //从begin开始加载8个项目
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ArrayList<Items>>() {
                        @Override
                        public void onCompleted() {
                            haowuSwipeRefreshLayout.setRefreshing(false);
                            //取消刷新动画
                            if(flag==true)
                            {
                            }
                            else
                            {
                                isLoadingMore=false;
                            }
                            isLoading=false;

                        }
                        @Override
                        public void onError(Throwable e) {
                            isLoading=false;
                        }
                        @Override
                        public void
                        onNext(ArrayList<Items> itemses) {
                            if(flag==true)//如果是上拉刷新，
                            {               //表明加载了8个
                                dianpuPage=8; //设置加载的起始位置为8
                                haowuList.clear(); //清除列表
                                isLoadingMore = false;
                            }
                            else //如果是 下拉自动加载，
                            {   //那么就直接将起始位置+8
                                dianpuPage+=8;
                            }
                            if(itemses.size()==0) //如果size=0，表明没有更多了
                            {
                                isLoadingMore=true;
//                            Toast.makeText(getApplicationContext(), isLoadingMore.toString(),Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getApplicationContext(),"没有更多了",Toast.LENGTH_SHORT).show();
                            }
                            else//否则的话，直接将东西加入列表中，并更新recyclerView
                            {
                                haowuList.addAll(itemses);
                                haowuAdapter.notifyDataSetChanged();
                            }


                        }
                    });
        }
    }

    private void setHaowuRecyclerView() {
//        ImageLoader.getInstance().clearMemoryCache();

        haowuList = new ArrayList<>();
        loadHaowuItem(0,true);

        //给DataBean类放数据，最后把装好数据的DataBean类放到集合里
//        DBService dbService= DBFactory.createDBService();
//        dbService.queryItemByTag(HAOWU_TAG,0,10)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ArrayList<Items>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(ArrayList<Items> itemses) {
//                        for(Items items:itemses)
//                        {
//                            haowuList.add(items);
//                            haowuAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
        haowuSwipeRefreshLayout=(SwipeRefreshLayout)haowu_view.findViewById(R.id.haowu_swipe_refresh_layout);
        haowuRecyclerView = (RecyclerView) haowu_view.findViewById(R.id.haowu_recyclerView);
        final RecyclerView.LayoutManager mLayoutManager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        haowuRecyclerView.setLayoutManager(mLayoutManager);
        //创建适配器Adapter对象  参数：1.上下文2.数据加载集合
        haowuAdapter = new CommonAdapter<Items>(Homepage.this, R.layout.haowu_item, haowuList)
        {
            @Override
            protected void convert(MyViewHolder holder, Items items, int position) {
                ImageView mIcon = holder.getView(R.id.haowu_item_image);
                TextView mTextView = holder.getView(R.id.haowu_item_text);
                ImageView mlikeIcon = holder.getView(R.id.haowu_item_dianzan);
                TextView mlikeNum = holder.getView(R.id.haowu_item_dianzan_num);
                ImageLoader.getInstance().displayImage(items.getItem_picture_url(),mIcon);
                mTextView.setText(items.getItem_name());
            }
        };

        //设置适配器
        haowuRecyclerView.setAdapter(haowuAdapter);
        /**
         * 如果在刷新，
         * 那么直接从第0个开始显示8个
         */
        haowuSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadHaowuItem(0,true);
            }
        });

        haowuRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] visibleItems = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                int lastitem = Math.max(visibleItems[0], visibleItems[1]);
                //isLoading==false表示没有在加载
                //isLoadingMore==false表示还有更多
                //lastVisibleItem >= totalItemCount - 2 表示剩下2个item自动加载
                // dy>0 表示向下滑动
                if (isLoading==false&&isLoadingMore==false && dy > 0 && (lastitem > haowuAdapter.getItemCount()-2) ) {

                    int temp=dianpuPage;
                    loadHaowuItem(temp,false);
                }
            }
        });

    }


    private void loadKetangItem(int begin,final Boolean flag)
    {
        if(!isLoading)
        {
            isLoading=true;
            DBService dbService= DBFactory.createDBService();
            dbService.queryArticalUserByTag("1", begin, 8)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ArrayList<ArticalAndUser>>()  {
                        @Override
                        public void onCompleted() {
                            ketangSwipeRefreshLayout.setRefreshing(false);
                            if(flag==true)
                            {
//                                Toast.makeText(getApplicationContext(),"刷新成功",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                isLoadingMore=false;
                                dianpuPage+=8;
                            }
                            isLoading=false;

                        }
                        @Override
                        public void onError(Throwable e) {
                            isLoading=false;
//                            Log.d("errrrrr",e.getMessage());

                        }
                        @Override
                        public void onNext(ArrayList<ArticalAndUser> articalAndUsers)  {
                            isLoading=true;
                            if(flag==true)
                            {
                                dianpuPage=8;
                                ketangList.clear();
                                isLoadingMore = false;
                            }

                            if(articalAndUsers.size()==0)
                            {
                                isLoadingMore=true;
//                            Toast.makeText(getApplicationContext(), isLoadingMore.toString(),Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getApplicationContext(),"没有更多了",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                ketangList.addAll(articalAndUsers);
                                ketangAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }

    }

    private void setKetangRecyclerView() {
//        ImageLoader.getInstance().clearMemoryCache();
        ketangList = new ArrayList<>();

        loadKetangItem(0, true);
        bestContentView=(ImageView)ketang_view.findViewById(R.id.best_content);
        ketangRecyclerView = (RecyclerView) ketang_view.findViewById(R.id.ketang_recyclerView);
        ketangSwipeRefreshLayout=(SwipeRefreshLayout)ketang_view.findViewById(R.id.ketang_swipe_refresh_layout);
        final StaggeredGridLayoutManager mLayoutManager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


        ketangRecyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setGapStrategy(GAP_HANDLING_NONE);

        //创建适配器Adapter对象  参数：1.上下文 2.数据加载集合
        ketangAdapter = new CommonAdapter<ArticalAndUser>(Homepage.this, R.layout.ketang_item, ketangList)
        {
            @Override
            protected void convert(MyViewHolder holder, ArticalAndUser articalAndUser, int position) {
                ImageView mArticalPic = holder.getView(R.id.ketang_item_image);
                ImageView mPeopleIcon = holder.getView(R.id.ketang_item_icon);
                TextView mPeopleName = holder.getView(R.id.ketang_item_name);
                TextView mTitle = holder.getView(R.id.ketang_item_content_title);
                TextView mDescription = holder.getView(R.id.ketang_item_content_description);
                TextView mCount = holder.getView(R.id.ketang_item_dianzan_text);

                ImageLoader.getInstance().displayImage(articalAndUser.getArtical().getArtical_picture_url(), mArticalPic);
                ImageLoader.getInstance().displayImage(articalAndUser.getUser().getUser_icon(), mPeopleIcon);
                mPeopleName.setText(articalAndUser.getUser().getUser_name());
                mTitle.setText(articalAndUser.getArtical().getArtical_title());
                mDescription.setText(articalAndUser.getArtical().getArtical_description());
                mCount.setText(String.valueOf(articalAndUser.getArtical().getArtical_artical_collection_count()));
            }
        };
        ketangAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                /**
                 * 跳转到文章详情页面
                 */
                Intent intent=new Intent(Homepage.this,ArticleDetail.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable(LOGIN_USER, loginUser);
                bundle.putSerializable(ARTICAL, ketangList.get(position).getArtical());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });

        //设置适配器
        ketangRecyclerView.setAdapter(ketangAdapter);

        ketangSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadKetangItem(0, true);
            }
        });

        ketangRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] visibleItems = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                int lastitem = Math.max(visibleItems[0], visibleItems[1]);
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (isLoading==false&&isLoadingMore==false && dy > 0 && (lastitem > ketangAdapter.getItemCount()-2) ) {
                    int temp=dianpuPage;

                    loadKetangItem(temp,false);
                    mLayoutManager.invalidateSpanAssignments(); //防止第一行到顶部有空白区域

                }
            }
        });
        bestContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Homepage.this,ArticleDetail.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable(LOGIN_USER, loginUser);
                bundle.putSerializable(ARTICAL, ketangList.get(2).getArtical());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    /**
     *
     * @param tag//用来分类的东西，如果是""---表示加载所有
     *           //如果是BREAKFAST_TAG 加载早餐
     *           //LUNCH_TAG 加载午餐
     *           //DINNER_TAG 加载晚餐
     *           //MIDNIGHT_SNACK_TAG 加载宵夜
     * @param begin //开始加载的子项
     * @param flag  //flag=true 上拉刷新 flag=false 下滑自动加载
     *
     */
    private void loadAllDianpuItem(String tag,int begin, final boolean flag)
    {

        if(!isLoading)
        {
            isLoading=true;
            DBService dbService= DBFactory.createDBService();
            dbService.queryItemByTag(tag,begin,8)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ArrayList<Items>>() {
                        @Override
                        public void onCompleted() {
                            dianpuswipeRefreshLayout.setRefreshing(false);
                            if(flag==true)
                            {
//                                Toast.makeText(getApplicationContext(),"刷新成功",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                isLoadingMore=false;
                                dianpuPage+=8;
                            }
                            isLoading=false;

                        }
                        @Override
                        public void onError(Throwable e) {
                            isLoading=false;
                            Log.d("errrrrr",e.getMessage());

                        }
                        @Override
                        public void
                        onNext(ArrayList<Items> itemses) {
                            if(flag==true)
                            {
                                dianpuPage=8;
                                dianpuList.clear();
                                isLoadingMore = false;
                            }

                            if(itemses.size()==0)
                            {
                                isLoadingMore=true;
//                            Toast.makeText(getApplicationContext(), isLoadingMore.toString(),Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getApplicationContext(),"没有更多了",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                dianpuList.addAll(itemses);
                                dianpuAdapter.notifyDataSetChanged();
                            }


                        }
                    });
        }
    }
    private void setDianpuRecyclerView() {
//        ImageLoader.getInstance().clearMemoryCache();

//        isLoadingMore=false;
//        dianpuPage=8;
//        isLoading=true;
        dianpuList = new ArrayList<>();
        loadAllDianpuItem("",0, true);
        dianpuRecyclerView = (RecyclerView) dianpu_view.findViewById(R.id.dianpu_list_recyclerView);
        dianpuswipeRefreshLayout = (SwipeRefreshLayout) dianpu_view.findViewById(R.id.dianpu_swipe_refresh_layout);
        final StaggeredGridLayoutManager mLayoutManager
                = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        dianpuRecyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setGapStrategy(GAP_HANDLING_NONE);//防止位置改变

        breakfast = (Button) dianpu_view.findViewById(R.id.breakfast_btn);
        lunch = (Button) dianpu_view.findViewById(R.id.lunch_btn);
        dinner = (Button) dianpu_view.findViewById(R.id.dinner_btn);
        midnight_snack = (Button) dianpu_view.findViewById(R.id.midnight_snack_btn);
        /*显示所有*/
        dianpuAdapter = new CommonAdapter<Items>(Homepage.this, R.layout.dianpu_item, dianpuList) {
            @Override
            protected void convert(MyViewHolder holder, Items items, int position) {
                ImageView dianpu_Image = holder.getView(R.id.dianpu_item_image);
                TextView dianpu_Text = holder.getView(R.id.dianpu_item_text);
                TextView dianpu_Price = holder.getView(R.id.dianpu_item_price);
                ImageLoader.getInstance().displayImage(items.getItem_picture_url(), dianpu_Image);
                dianpu_Text.setText(items.getItem_name());
                dianpu_Price.setText(items.getItem_price().toString());
            }
        };
        dianpuAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                /**
                 * 跳转到评论页面
                 */
                Intent intent = new Intent(Homepage.this, ItemCommentList.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(LOGIN_USER, loginUser);
                bundle.putSerializable(DIANPU_ITEM, dianpuList.get(position).getItem_id());
                intent.putExtras(bundle);
                startActivityForResult(intent, DIANPU_ITEM_REQUEST_CODE);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        dianpuRecyclerView.setAdapter(dianpuAdapter);
        final String[] TAG = {""};
        /*显示早餐*/
        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadAllDianpuItem(BREAKFAST_TAG,0, true);
                TAG[0] =BREAKFAST_TAG;
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dianpuList.clear();
                loadAllDianpuItem(LUNCH_TAG,0, true);
                TAG[0] =LUNCH_TAG;
//                DBService dbService = DBFactory.createDBService();
//                dbService.queryItemByTag(LUNCH_TAG, 0, 10)
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Subscriber<ArrayList<Items>>() {
//                            @Override
//                            public void onCompleted() {
//                                dianpuAdapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onNext(ArrayList<Items> itemses) {
//                                dianpuList.clear();
//                                dianpuList.addAll(itemses);
//                                dianpuAdapter.notifyDataSetChanged();
//                            }
//                        });
            }
        });
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dianpuList.clear();
                loadAllDianpuItem(DINNER_TAG,0, true);
                TAG[0] =DINNER_TAG;
//                DBService dbService = DBFactory.createDBService();
//                dbService.queryItemByTag(DINNER_TAG, 0, 10)
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Subscriber<ArrayList<Items>>() {
//                            @Override
//                            public void onCompleted() {
//                                dianpuAdapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onNext(ArrayList<Items> itemses) {
//                                dianpuList.clear();
//                                dianpuList.addAll(itemses);
//                                dianpuAdapter.notifyDataSetChanged();
//                            }
//                        });
            }
        });
        midnight_snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAllDianpuItem(MIDNIGHT_SNACK_TAG,0,true);
                TAG[0] =MIDNIGHT_SNACK_TAG;

//                dianpuList.clear();
//                DBService dbService = DBFactory.createDBService();
//                dbService.queryItemByTag(MIDNIGHT_SNACK_TAG, 0, 10)
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Subscriber<ArrayList<Items>>() {
//                            @Override
//                            public void onCompleted() {
//                                dianpuAdapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onNext(ArrayList<Items> itemses) {
//                                dianpuList.clear();
//                                dianpuList.addAll(itemses);
//                                dianpuAdapter.notifyDataSetChanged();
//                            }
//                        });
            }
        });

        dianpuswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAllDianpuItem(TAG[0],0, true);
            }
        });

        dianpuRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] visibleItems = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                int lastitem = Math.max(visibleItems[0], visibleItems[1]);
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (isLoading==false&&isLoadingMore==false && dy > 0 && (lastitem > dianpuAdapter.getItemCount()-2) ) {

                    int temp=dianpuPage;
                    loadAllDianpuItem(TAG[0],temp,false);
                }
                mLayoutManager.invalidateSpanAssignments(); //防止第一行到顶部有空白区域

            }
        });


    }

    private void setGouwuRecyclerView() {
//        RadioButton select = (RadioButton) gouwu_view.findViewById(R.id.gouwu_item_select);
//        Button sub = (Button) gouwu_view.findViewById(R.id.gouwu_item_sub);
//        Button add = (Button) gouwu_view.findViewById(R.id.gouwu_item_add);
//        ImageLoader.getInstance().clearMemoryCache();
//        SwitchCompat select_all = (SwitchCompat) gouwu_view.findViewById(R.id.gouwu_select_all);
        total_price =  new BigDecimal("0");
        payForIt=(Button)gouwu_view.findViewById(R.id.gouwu_getsum);
        final TextView total_price_text = (TextView) gouwu_view.findViewById(R.id.total_price);
        //设置总价textView
        total_price_text.setText(total_price.toString());

//        //设置全选按钮
//        select_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                gouwuAdapter
//            }
//        });
        final List<ShoppingcarAndItem> gouwuList = new ArrayList<>();
        //给DataBean类放数据，最后把装好数据的DataBean类放到集合里
        final DBService dbService= DBFactory.createDBService();
        dbService.queryShoppingcarAndItem(loginUser.getUser_id(),0,10)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<ShoppingcarAndItem>>() {
                               @Override
                               public void onCompleted() {
                                   if(gouwuRecyclerView!=null)gouwuAdapter.notifyDataSetChanged();

                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onNext(ArrayList<ShoppingcarAndItem> shoppingcarAndItems) {
                                   gouwuList.addAll(shoppingcarAndItems);
                               }
                           });

        gouwuRecyclerView = (RecyclerView) gouwu_view.findViewById(R.id.gouwu_recyclerView);
        gouwuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        gouwuAdapter = new CommonAdapter<ShoppingcarAndItem>(Homepage.this, R.layout.gouwu_item, gouwuList)
        {
            @Override
            protected void convert(MyViewHolder holder, final ShoppingcarAndItem shoppingcarAndItem, int position) {
                ImageView itemPic = holder.getView(R.id.gouwu_item_image);
                final TextView price = holder.getView(R.id.gouwu_item_price);
                final TextView num = holder.getView(R.id.gouwu_item_num);
                final SwitchCompat select = holder.getView(R.id.gouwu_item_select);
                final TextView name=holder.getView(R.id.gouwu_itme_description);
                Button sub = holder.getView(R.id.gouwu_item_sub);
                Button add = holder.getView(R.id.gouwu_item_add);

                select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                        {
//                            Log.d("item_count",Long.toString(shoppingcarAndItem.shoppingCar.getShoppingcar_count()));
//                            Log.d("item_price",shoppingcarAndItem.items.getItem_price().toString());
//                            Log.d("add",shoppingcarAndItem.items.getItem_price().multiply(new BigDecimal(shoppingcarAndItem.shoppingCar.getShoppingcar_count())).toString());
                            total_price = total_price.add(shoppingcarAndItem.items.getItem_price().multiply(new BigDecimal(shoppingcarAndItem.shoppingCar.getShoppingcar_count())));

//                            Log.d("0+add",total_price.toString());
                        }
                        else
                        {
                            total_price = total_price.subtract(shoppingcarAndItem.items.getItem_price().multiply(new BigDecimal(shoppingcarAndItem.shoppingCar.getShoppingcar_count())));
                        }
                        total_price_text.setText(total_price.toString());
//                        Toast.makeText(getApplicationContext(),total_price.toString(),Toast.LENGTH_SHORT).show();
                    }
                });

                sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(shoppingcarAndItem.shoppingCar.getShoppingcar_count()>1)
                        {
                            Log.d("item_id",Long.toString(shoppingcarAndItem.items.getItem_id()));
                            dbService.removeItemFromShoppingcar(shoppingcarAndItem.getShoppingCar().getShoppingcar_user_id(),shoppingcarAndItem.items.getItem_id(),1)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<Boolean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(Boolean aBoolean) {

                                        }
                                    });
                            shoppingcarAndItem.shoppingCar.setShoppingcar_count(shoppingcarAndItem.shoppingCar.getShoppingcar_count()-1);
                            num.setText(Long.toString(shoppingcarAndItem.shoppingCar.getShoppingcar_count()));
                            if(select.isChecked())
                            {
                                total_price = total_price.subtract(shoppingcarAndItem.items.getItem_price());
                                total_price_text.setText(total_price.toString());
                            }
                        }

                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dbService.addItemToShoppingcar(new ShoppingCar(shoppingcarAndItem.shoppingCar.getShoppingcar_user_id(),shoppingcarAndItem.items.getItem_id(),1))
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Boolean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Boolean aBoolean) {

                                    }
                                });
                        shoppingcarAndItem.shoppingCar.setShoppingcar_count(shoppingcarAndItem.shoppingCar.getShoppingcar_count()+1);
                        num.setText(Long.toString(shoppingcarAndItem.shoppingCar.getShoppingcar_count()));
                        if(select.isChecked())
                        {
                            total_price = total_price.add(shoppingcarAndItem.items.getItem_price());
                            total_price_text.setText(total_price.toString());
                        }

                    }
                });

                ImageLoader.getInstance().displayImage(shoppingcarAndItem.getItems().getItem_picture_url(),itemPic);
                price.setText(String.valueOf(shoppingcarAndItem.getItems().getItem_price()));
                num.setText(String.valueOf(shoppingcarAndItem.getShoppingCar().getShoppingcar_count()));
                name.setText(shoppingcarAndItem.getItems().getItem_name());

            }
        };
        gouwuAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onLongClick(final int position) {
                final SweetAlertDialog msweetAlertDialog=new SweetAlertDialog(Homepage.this);
                msweetAlertDialog.setTitleText("移除购物车")
                        .setContentText("是否从购物车中移除 "+gouwuList.get(position).getItems().getItem_name())
                        .setConfirmText("确定删除")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                String title=gouwuList.get(position).getItems().getItem_name();
                                   dbService.removeItemFromShoppingcar
                                           (gouwuList.get(position).getShoppingCar().getShoppingcar_user_id(),
                                           gouwuList.get(position).getItems().getItem_id(),
                                           gouwuList.get(position).getShoppingCar().getShoppingcar_count())
                                    .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                           .subscribe(new Subscriber<Boolean>() {
                                               @Override
                                               public void onCompleted() {

                                               }

                                               @Override
                                               public void onError(Throwable e) {

                                               }

                                               @Override
                                               public void onNext(Boolean aBoolean) {
                                                   if(aBoolean.booleanValue())
                                                   {
                                                       gouwuList.remove(position);
                                                       gouwuAdapter.notifyDataSetChanged();
                                                   }
                                               }
                                           });
                                sweetAlertDialog.setTitleText("移除成功")
                                        .setContentText("你已经成功删除"+title)
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                msweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });

        gouwuRecyclerView.setAdapter(gouwuAdapter);
        payForIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog msweetAlertDialog=new SweetAlertDialog(Homepage.this);
                msweetAlertDialog.setTitleText("确认支付")
                        .setContentText("您需要支付"+total_price_text.getText().toString())
                        .setConfirmText("确认支付")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                dbService.pay(loginUser.getUser_id(),total_price)
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<Boolean>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(Boolean aBoolean) {
                                                if (aBoolean.booleanValue()) {
                                                    sweetAlertDialog.setTitleText("支付成功")
                                                            .setContentText("")
                                                            .setConfirmText("确定")
                                                            .setConfirmClickListener(null)
                                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                }
                                                else
                                                {
                                                    sweetAlertDialog.setTitleText("余额不足")
                                                            .setContentText("")
                                                            .setConfirmText("确定")
                                                            .setConfirmClickListener(null)
                                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                }
                                            }
                                        });
                            }
                        })
                        .setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                msweetAlertDialog.dismissWithAnimation();
                            }
                        }).show();

            }
        });

    }

    private void loadArtical(int begin,final Boolean flag)
    {
        if(!isLoading)
        {
            isLoading=true;
            DBService dbService= DBFactory.createDBService();
            dbService.queryArticalUserByUserId(loginUser.getUser_id(),begin,8)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ArrayList<ArticalAndUser>>() {
                        @Override
                        public void onCompleted() {
                            wodeRefreshLayout.setRefreshing(false);
                            if(flag==true)
                            {
//                                Toast.makeText(getApplicationContext(),"刷新成功",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                isLoadingMore=false;
                            }
                            isLoading=false;
                        }
                        @Override
                        public void onError(Throwable e) {
                            isLoading=false;
//                            Log.d("errrrrr",e.getMessage());
                        }
                        @Override
                        public void
                        onNext(ArrayList<ArticalAndUser> articalAndUsers) {
                            if(flag==true)
                            {
                                dianpuPage=8;
                                wodeList.clear();
                                isLoadingMore = false;
                            }
                            else
                            {
                                dianpuPage+=8;
                            }
                            if(articalAndUsers.size()==0)
                            {
                                isLoadingMore=true;
//                            Toast.makeText(getApplicationContext(), isLoadingMore.toString(),Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getApplicationContext(),"没有更多了",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                wodeList.addAll(articalAndUsers);
                                wodeAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }
    private void setArticaleRecyclerView() {
        wodeList = new ArrayList<>();
        loadArtical(0,true);
//        DBService dbService = DBFactory.createDBService();
//        dbService.queryArticalUserByUserId(loginUser.getUser_id(),0,10)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ArrayList<ArticalAndUser>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(ArrayList<ArticalAndUser> articalAndUsers) {
//                        wodeList.clear();
//                        wodeList.addAll(articalAndUsers);
//                        wodeAdapter.notifyDataSetChanged();
//                    }
//                });
        wodeRecyclerView = (RecyclerView) wode_view.findViewById(R.id.my_data_recyclerView);
        wodeRefreshLayout=(SwipeRefreshLayout)wode_view.findViewById(R.id.wode_swipe_refresh_layout);
        final LinearLayoutManager mLayoutManager=new LinearLayoutManager(this);
        wodeRecyclerView.setLayoutManager(mLayoutManager);
        wodeAdapter=new CommonAdapter<ArticalAndUser>(Homepage.this, R.layout.article_item, wodeList) {
            @Override
            protected void convert(MyViewHolder holder, ArticalAndUser articalAndUser, int position) {
                TextView articalTitle=holder.getView(R.id.article_title);
                TextView articalDescription=holder.getView(R.id.article_description);

                articalTitle.setText(articalAndUser.getArtical().getArtical_title());
                articalDescription.setText(articalAndUser.getArtical().getArtical_description());
            }
        };
        wodeAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(Homepage.this,ArticleDetail.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable(LOGIN_USER, loginUser);
                bundle.putSerializable(ARTICAL, wodeList.get(position).getArtical());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(final int position) {
                SweetAlertDialog msweetAlertDialog=new SweetAlertDialog(Homepage.this);
                final String title=wodeList.get(position).getArtical().getArtical_title();
                msweetAlertDialog.setTitleText("删除文章确认")
                        .setContentText("是否删除文章"+wodeList.get(position).getArtical().getArtical_title())
                        .setConfirmText("确认删除")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                DBService tempService=DBFactory.createDBService();
                                tempService.deleteArtical(wodeList.get(position).getArtical().getArtical_id())
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<Boolean>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(Boolean aBoolean) {
                                                if(aBoolean.booleanValue())
                                                {
                                                    wodeList.remove(position);
                                                    wodeAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                                sweetAlertDialog.setTitleText("删除成功")
                                        .setContentText("你已经成功删除"+title)
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        }).show();


            }
        });
        wodeRecyclerView.setAdapter(wodeAdapter);

        wodeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadArtical(0,true);
            }
        });
        wodeRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (isLoading==false&&isLoadingMore==false&&lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    int temp=dianpuPage;
                    loadArtical(temp,false);
                }
            }
        });


    }

    private void initUserData()
    {
        final ImageView personIcon = (ImageView) wode_view.findViewById(R.id.my_data_icon);
        final TextView personName = (TextView) wode_view.findViewById(R.id.my_data_name);
        final ImageView personSex = (ImageView) wode_view.findViewById(R.id.my_data_sex);
        final TextView city = (TextView) wode_view.findViewById(R.id.my_data_city);;
        final TextView secondName = (TextView) wode_view.findViewById(R.id.my_data_second_name);
        final TextView leftPrice = (TextView) wode_view.findViewById(R.id.my_data_price);
        final TextView articalNum = (TextView) wode_view.findViewById(R.id.my_data_aritical);
        final FloatingActionButton floatingActionButton = (FloatingActionButton) wode_view.findViewById(R.id.my_data_floatAction);

        Bundle mBundle = getIntent().getExtras();
        DBService dbService = DBFactory.createDBService();
        dbService.queryUserById(((User)mBundle.get(LOGIN_USER)).getUser_id())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        ImageLoader.getInstance().displayImage(loginUser.getUser_icon(), personIcon);
                        personName.setText(loginUser.getUser_name());
                        city.setText(loginUser.getUser_city());
                        secondName.setText(loginUser.getUser_description());
                        leftPrice.setText(loginUser.getUser_account().toString());
                        if(loginUser.isUser_gender()==true) {
                            personSex.setImageResource(R.drawable.male);
                        }
                        else {
                            personSex.setImageResource(R.drawable.female);
                        }
                        secondName.setText(loginUser.getUser_description());
                        leftPrice.setText(String.valueOf(loginUser.getUser_account()));
                        articalNum.setText(String.valueOf(loginUser.getUser_artical_count()));

                        setArticaleRecyclerView();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(User user) {
                        loginUser.setUser(user);
                    }
                });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, EditMessage.class);
                Bundle bundle = new Bundle();
                intent.putExtra("USER_ID",loginUser.getUser_id());
                startActivityForResult(intent,REQUEST_EDIT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_EDIT&&resultCode==RESULT_EDIT)
        {
            initUserData();
        }
    }
}
