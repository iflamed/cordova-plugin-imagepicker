/**
 * An Image Picker Plugin for Cordova/PhoneGap.
 */
package com.synconset;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import me.iwf.photopicker.PhotoPickUtils;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import me.iwf.photopicker.utils.PhotoPickerIntent;

import com.mengran.shoubanjiang.Cache;

public class ImagePicker extends CordovaPlugin {
	public static String TAG = "ImagePicker";

	private CallbackContext callbackContext;
	private JSONObject params;

	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
		 this.callbackContext = callbackContext;
		 this.params = args.getJSONObject(0);
		if (action.equals("getPictures")) {
            int max = 20;
            if (this.params.has("maxImages")) {
             max = this.params.getInt("maxImages");
            }
			Intent intent = new Intent(cordova.getActivity(), PhotoPickerActivity.class);
	        PhotoPickerIntent.setPhotoCount(intent, max);
	        PhotoPickerIntent.setColumn(intent, 4);
			if (this.cordova != null) {
				this.cordova.startActivityForResult((CordovaPlugin) this, intent, PhotoPicker.REQUEST_CODE);
			}
		}
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PhotoPicker.REQUEST_CODE) {//第一次，选择图片后返回
                if (data != null) {
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    // ArrayList<String> photosList = new ArrayList<String>();
                    // Iterator<String> it = photos.iterator();
                    // while (it.hasNext()) {
                    //     String uuid = "/" + UUID.randomUUID().toString();
                    //     Cache.getInstance().images.put(uuid, it.next());
                    //     photosList.add(uuid);
                    // }
                    JSONArray res = new JSONArray(photos);
                    this.callbackContext.success(res);
                } else {
                    this.callbackContext.error("选择图片失败");
                }
            } else if (requestCode == PhotoPreview.REQUEST_CODE){//如果是预览与删除后返回
                if (data != null) {
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                }
                this.callbackContext.error("选择图片之后点击完成确认操作！");
            }
        } else {

            if (requestCode == PhotoPicker.REQUEST_CODE){
                JSONArray res = new JSONArray();
                this.callbackContext.success(res);
            }
        }
	}
}
