package com.example.administrator.meetingtaste.service;

import com.example.administrator.meetingtaste.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/12/21.
 */

public interface DBService {

    /**
     * Insert接口
     */

    //新增user 需要参数id,icon,name,password,gender
    @POST("insert?func=addUser")
    Observable<Boolean> addUser(@Body User user);

    //新增items 需要的参数picture_url,name,description,creator_user_id,price
    @POST("insert?func=addItem")
    Observable<Boolean> addItem(@Body Items item);

    //发表原创文章 需要参数artical_user_id,artical_url
    @POST("insert?func=addOriginalArtical")
    Observable<Boolean> addOriginalArtical(@Body Artical artical);

    //转发文章 需要参数user_id,forward_src
    @POST("insert?func=forwardArtical")
    Observable<Boolean> forwardArtical(@Body Artical artical);

    //新增物品评论 需要参数item_id,user_id,comment_content
    @POST("insert?func=addItemComment")
    Observable<Boolean> addItemComment(@Body ItemComment itemComment);

    //发表文章评论 需要参数artical_id  user_id  comment_content
    @POST("insert?func=addArticalComment")
    Observable<Boolean> addArticalComment(@Body ArticalComment articalComment);

    //添加item到购物车 需要参数user_id item_id count
    @POST("insert?func=addItemToShoppingcar")
    Observable<Boolean> addItemToShoppingcar(@Body ShoppingCar shoppingCar);


    /**
     * Delete接口
     */

    //根据用户id删除用户 需要参数user_id user_password
    @POST("delete?func=deleteuser")
    @FormUrlEncoded
    Observable<Boolean> deleteUser(@Field(User.USER_ID) String user_id,@Field(User.USER_PASSWORD)String user_password);

    //删除item 需要参数item_id
    @GET("delete?func=deleteItem")
    Observable<Boolean> deleteItem(@Query(Items.ITEM_ID)long item_id);

    //删除文章 需要参数artical_id
    @GET("delete?func=deleteArtical")
    Observable<Boolean> deleteArtical(@Query(Artical.ARTICAL_ID)long artical_id);

    //删除物品评论 需要参数item_comment_id
    @GET("delete?func=deleteItemComment")
    Observable<Boolean> deleteItemComment(@Query(ItemComment.ITEM_COMMENT_ID)long item_comment_id);

    //删除文章评论 需要参数artical_comment_id
    @GET("delete?func=deleteArticalComment")
    Observable<Boolean> deleteArticalComment(@Query(ArticalComment.ARTICAL_COMMENT_ID)long artical_comment_id);

    //从购物车去除若干数量的item 需要参数user_id item_id item_count
    @GET("delete?func=removeItemFromShoppingcar")
    Observable<Boolean> removeItemFromShoppingcar(@Query(ShoppingCar.SHOPPINGCAR_USER_ID)String user_id,@Query(ShoppingCar.SHOPPINGCAR_ITEM_ID)long item_id,@Query(ShoppingCar.SHOPPINGCAR_COUNT)long count);

    /**
     * Update接口
     */

    //更新user信息 需要参数user_id user_icon user_name user_phone user_address user_gender
    @POST("update?func=updateUser")
    Observable<Boolean> updateUser(@Body User user);

    //更新items表 需要参数:id,picture_url,name,description,price
    @POST("update?func=updateItesm")
    Observable<Boolean> updateItem(@Body Items items);

    @GET("update?func=pay")
    Observable<Boolean> pay(@Query(User.USER_ID)String user_id, @Query("pay_account")BigDecimal pay_account);


    /**
     * Show接口
     */
    //根据id查询某个user  需要参数user_id
    @GET("show?func=queryUserById")
    Observable<User> queryUserById(@Query(User.USER_ID)String user_id);

    //根据artical_id查询artical 需要参数artical_id
    @GET("show?func=queryArtical")
    Observable<Artical> queryArtical(@Query(Artical.ARTICAL_ID)long artical_id);

    //根据tag来查找某个范围的artical 需要参数tag(tag中各个项目用空格分隔) begin count
    @GET("show?func=queryArticalByTag")
    Observable<ArrayList<Artical>> queryArticalByTag(@Query(Artical.ARTICAL_TAG)String artical_tag,@Query("begin")int begin,@Query("count")int count);

    //根据item_id来查询Items 需要参数item_id
    @GET("show?func=queryItemByItemId")
    Observable<Items> queryItemByItemId(@Query(Items.ITEM_ID)long item_id);

    //根据tag来查询item 需要参数tag begin count
    @GET("show?func=queryItemByTag")
    Observable<ArrayList<Items>> queryItemByTag(@Query(Items.ITEM_TAG)String item_tag,@Query("begin")int begin,@Query("count")int count);

    //获取最新的items 需要参数begin count
    @GET("show?func=queryItems")
    Observable<ArrayList<Items>> queryItems(@Query("begin")int begin,@Query("count")int count);

    //根据artical_id 查询某个范围内的artical_comment 需要参数artical_id begin count
    @GET("show?func=queryArticalCommentByArticalId")
    Observable<ArrayList<ArticalComment>> queryArticalCommentByArticalId(@Query(ArticalComment.ARTICAL_COMMENT_ARTICAL_ID)long artical_id,@Query("begin")int begin,@Query("count")int count);

    //根据artical_id 查询某个范围的artical_collection 需要参数artical_id begin count
    @GET("show?func=queryArticalCollectionByArticalId")
    Observable<ArrayList<ArticalCollection>> queryArticalCollectionByArticalId(@Query(ArticalCollection.ARTICAL_COLLECTION_ARTICAL_ID)long artical_id,@Query("begin")int begin,@Query("count")int count);

    //根据user_id查询articalcollection 需要参数user_id begin count
    @GET("show?func=queryArticalCollectionByUserId")
    Observable<ArrayList<ArticalCollection>> queryArticalCollectionByUserId(@Query(ArticalCollection.ARTICAL_COLLECTION_USER_ID)String user_id,@Query("begin")int begin,@Query("count")int count);

    //根据item_id 查询某个范围的评论表 需要参数item_id begin count
    @GET("show?func=queryItemCommentByItemId")
    Observable<ArrayList<ItemComment>> queryItemCommentByItemId(@Query(ItemComment.ITEM_COMMENT_ITEM_ID)long item_id,@Query("begin")int begin,@Query("count")int count);

    //查询某用户购物车 需要参数user_id  begin,count
    @GET("show?func=queryItemFromShoppingcar")
    Observable<ArrayList<Items>> queryItemFromShoppingcar(@Query(ShoppingCar.SHOPPINGCAR_USER_ID)String user_id,@Query("begin")int begin,@Query("count")int count);

    //根据user_id查找文章 返回user和artical相关信息
    @GET("show?func=queryArticalUserByUserId")
    Observable<ArrayList<ArticalAndUser>> queryArticalUserByUserId(@Query(User.USER_ID)String user_id,@Query("begin")int begin,@Query("count")int count);

    //根据tag查找文章 返回user和artical相关信息 需要参数tag begin count
    @GET("show?func=queryArticalUserByTag")
    Observable<ArrayList<ArticalAndUser>> queryArticalUserByTag(@Query(Artical.ARTICAL_TAG)String tag,@Query("begin")int begin,@Query("count")int count);

    //根据artical_id 查询某个范围内的artical_comment 需要参数artical_id begin count
    @GET("show?func=queryArticalCommentAndUser")
    Observable<ArrayList<ArticalCommentAndUser>> queryArticalCommentAndUser(@Query(ArticalComment.ARTICAL_COMMENT_ARTICAL_ID)long artical_id,@Query("begin")int begin,@Query("count")int count);

    //根据item_id 查询某个范围的评论表 需要参数item_id begin count
    @GET("show?func=queryItemCommentAndUser")
    Observable<ArrayList<ItemCommentAndUser>> queryItemCommentAndUser(@Query(ItemComment.ITEM_COMMENT_ITEM_ID)long item_id,@Query("begin")int begin,@Query("count")int count);

    //根据user_id获取购物车列表 需要参数user_id begin count
    @GET("show?func=queryShoppingcarAndItem")
    Observable<ArrayList<ShoppingcarAndItem>> queryShoppingcarAndItem(@Query(ShoppingCar.SHOPPINGCAR_USER_ID)String user_id,@Query("begin")int begin,@Query("count")int count);


    /**
     * other
     */
    //用户登录 需要参数user_id user_password
    @GET("other?func=loginUser")
    Observable<User> loginUser(@Query(User.USER_ID) String user_id,@Query(User.USER_PASSWORD) String user_password);


    /**
     * upload
     */
    //上传用户头像 需要参数user_id 图片
    @Multipart
    @POST("upload")
    Observable<Boolean> uploadUserIcon(@Part List<MultipartBody.Part> partList);

}