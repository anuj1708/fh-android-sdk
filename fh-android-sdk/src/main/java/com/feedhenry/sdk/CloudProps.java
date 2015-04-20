package com.feedhenry.sdk;

import org.json.fh.JSONObject;

import com.feedhenry.sdk.utils.FHLog;

public class CloudProps {

  private JSONObject mCloudProps;
  private String mHostUrl;

  private static final String LOG_TAG = "com.feedhenry.sdk.CloudProps";

  private static String INVALID_URI_PATTERN = "(_.*?)\\.";

  private static CloudProps mInstance;

  private CloudProps(JSONObject pCloudProps) {
    mCloudProps = pCloudProps;
  }

  /**
   * Construct a cloudProps instance for local development. The cloud url will be the value of
   * "host" specified in
   * assets/fhconfig.local.properties file.
   *
   */
  private CloudProps() {
    mCloudProps = new JSONObject();
    mCloudProps.put("url", AppProps.getInstance().getHost());
  }

  /**
   * Return the cloud host of the app
   *
   * @return the cloud host (no trailing "/")
   */
  public String getCloudHost() {
    if (null == mHostUrl) {
      String hostUrl = null;
      try {
        if (mCloudProps.has("url")) {
          hostUrl = mCloudProps.getString("url");
        } else {
          String appMode = AppProps.getInstance().getAppMode();
          JSONObject hosts = mCloudProps.getJSONObject("hosts");
          if (hosts.has("url")) {
            hostUrl = hosts.getString("url");
          } else {
            if ("dev".equalsIgnoreCase(appMode)) {
              hostUrl = hosts.getString("debugCloudUrl");
            } else {
              hostUrl = hosts.getString("releaseCloudUrl");
            }
          }
        }
        hostUrl = hostUrl.endsWith("/") ? hostUrl.substring(0, hostUrl.length() - 1) : hostUrl;
        //previously cloud host url could look like this: testing-nge0bsskhnq2slb3b1luvbwr-dev_testing.df.dev.e111.feedhenry.net
        //however, "_" is not valid in JAVA as URI host, that will cause the parsed URI contains null host.
        //since dynofarm now accept urls like this: testing-nge0bsskhnq2slb3b1luvbwr-dev.df.dev.e111.feedhenry.net
        //we need to remove the "_" + dynomame part if it exists
        hostUrl = hostUrl.replaceFirst(INVALID_URI_PATTERN, ".");
        mHostUrl = hostUrl;
        FHLog.v(LOG_TAG, "host url = " + mHostUrl);
      } catch (Exception e) {
        FHLog.e(LOG_TAG, e.getMessage(), e);
      }
    }

    return mHostUrl;
  }

  public static CloudProps init(){
    if(null == mInstance){
      mInstance = new CloudProps();
    }
    return mInstance;
  }

  public static CloudProps init(JSONObject pCloudProps){
    if(null == mInstance){
      mInstance = new CloudProps(pCloudProps);
    }
    return  mInstance;
  }

  public static CloudProps getInstance(){
    return mInstance;
  }
}
