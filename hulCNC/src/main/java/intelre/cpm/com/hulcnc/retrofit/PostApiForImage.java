package intelre.cpm.com.hulcnc.retrofit;

import retrofit.http.POST;

/**
 * Created by jeevanp on 28-12-2017.
 */

public interface PostApiForImage {
    @POST("Uploadimages")
    retrofit.Call<String> getUploadImage(@retrofit.http.Body com.squareup.okhttp.RequestBody reqesBody);

}
