package intelre.cpm.com.intelre.retrofit;

import intelre.cpm.com.intelre.constant.CommonString;
import okhttp3.ResponseBody;

/**
 * Created by jeevanp on 19-05-2017.
 */


//using interface for post data
public interface PostApi {
    @retrofit2.http.POST(CommonString.KEY_LOGIN_DETAILS)
    retrofit2.Call<ResponseBody> getLogindetail(@retrofit2.http.Body okhttp3.RequestBody request);
   /* @Multipart
    @POST("Uploadimages")
    Call<ResponseBody> getUploadImage(@Body RequestBody request);*/

    @retrofit2.http.POST("Uploadimages")
    retrofit2.Call<String> getUploadImage(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("DownloadAll")
    retrofit2.Call<ResponseBody> getDownloadAll(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("CoverageDetail_latest")
    retrofit2.Call<ResponseBody> getCoverageDetail(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("UploadJCPDetail")
    retrofit2.Call<ResponseBody> getUploadJCPDetail(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("UploadJsonDetail")
    retrofit2.Call<ResponseBody> getUploadJsonDetail(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("CoverageStatusDetail")
    retrofit2.Call<ResponseBody> getCoverageStatusDetail(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("Upload_StoreGeoTag_IMAGES")
    retrofit2.Call<ResponseBody> getGeoTagImage(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("CheckoutDetail")
    retrofit2.Call<ResponseBody> getCheckout(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("DeleteCoverage")
    retrofit2.Call<ResponseBody> deleteCoverageData(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("CoverageNonworking")
    retrofit2.Call<ResponseBody> setCoverageNonWorkingData(@retrofit2.http.Body okhttp3.RequestBody request);
}

