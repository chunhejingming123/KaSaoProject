package com.kasao.qintai.widget.photo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AlbumHelper {
	private Context context;
	private ContentResolver cr;
	private static AlbumHelper instance;
	private boolean hasBuildBucketList = false;
	// 缩略图列表
	HashMap<String, String> thumbnailMap = new HashMap<String, String>();
	HashMap<String, ImageBucket> bucketMap = new HashMap<String, ImageBucket>();
	private List<ImageItem> imageList = new ArrayList<ImageItem>();

	public static AlbumHelper getHelper() {
		if (instance == null) {
			instance = new AlbumHelper();
		}
		return instance;
	}

	public void init(Context context) {
		if (this.context == null)
			this.context = context;
		this.cr = context.getContentResolver();
	}

	private void getThumbnail() {
		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
				Thumbnails.DATA };
		Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
				null, null, "image_id DESC");
		if(cursor==null){
			return;
		}
		getThumbnailColumnData(cursor);
	}

	private void getThumbnailColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
//			int _id;
			int image_id;
			String imagePath;
			do {
//				_id = cur.getInt(cur.getColumnIndex(Thumbnails._ID));
				image_id = cur.getInt(cur.getColumnIndex(Thumbnails.IMAGE_ID));
				imagePath = cur.getString(cur.getColumnIndex(Thumbnails.DATA));
				thumbnailMap.put("" + image_id, imagePath);

			} while (cur.moveToNext());
		}
	}

	public void buildImagesBucketList() {
		getThumbnail();
		String[] columns = { Media._ID, Media.BUCKET_ID, Media.DATA,
				Media.DISPLAY_NAME, Media.TITLE, Media.SIZE,
				Media.BUCKET_DISPLAY_NAME, Media.DATE_ADDED };
		Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, "_id DESC");
		if(cur==null){
			return;
		}
		if (cur.moveToFirst()) {
			int photoIdIndex = cur.getColumnIndexOrThrow(Media._ID);
			int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
//			int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
//			int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
//			int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
//			int photoAddDate = cur.getColumnIndexOrThrow(Media.DATE_ADDED);
			int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
			int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
//			int totalNum = cur.getCount();
			do {
				String id = cur.getString(photoIdIndex);
				String path = cur.getString(photoPathIndex);
//				String name = cur.getString(photoNameIndex);
//				String title = cur.getString(photoTitleIndex);
//				String size = cur.getString(photoSizeIndex);
//				String addDate = cur.getString(photoAddDate);
				String bucketId = cur.getString(bucketIdIndex);
				String bucketname = cur.getString(bucketDisplayNameIndex);
				ImageItem i = new ImageItem();
				i.imageId = id;
				i.imagePath = path;
				i.thumbnailPath = thumbnailMap.get(id);
				imageList.add(i);
				ImageBucket bucket = null;
				if (this.bucketMap.containsKey(bucketId))
					bucket = this.bucketMap.get(bucketId);
				else {
					bucket = new ImageBucket();
					bucketMap.put(bucketId, bucket);
					bucket.bucketName = bucketname;
					bucket.imageList = new ArrayList<ImageItem>();
				}
				bucket.count++;
				bucket.imageList.add(i);
			} while (cur.moveToNext());
		}
	}

	public List<ImageBucket> getImageBucketList(boolean refresh) {
		if (refresh || (!refresh && !hasBuildBucketList)) {
			clear();
			buildImagesBucketList();
		}
		List<ImageBucket> bucketList = new ArrayList<ImageBucket>();
		ImageBucket allBucket = new ImageBucket();
		allBucket.bucketName = "我的图片";
		allBucket.imageList = imageList;
		allBucket.count = imageList.size();
		allBucket.isSelected = true;
		bucketList.add(allBucket);
		Iterator<Entry<String, ImageBucket>> itr = bucketMap.entrySet()
				.iterator();
		while (itr.hasNext()) {
			Map.Entry<String, ImageBucket> entry = itr.next();
			bucketList.add(entry.getValue());
		}
		return bucketList;

	}

	public List<ImageItem> getAllImageList(boolean refresh) {
		if (refresh || (!refresh && !hasBuildBucketList)) {
			clear();
			buildImagesBucketList();
		}
		return imageList;
	}

	public String getOriginalImagePath(String imageId) {
		String path = null;
		String[] projection = { Media._ID, Media.DATA };
		Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection,
				Media._ID + "=" + imageId, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			path = cursor.getString(cursor.getColumnIndex(Media.DATA));
		}
		return path;
	}

	public void clear() {
		thumbnailMap.clear();
		bucketMap.clear();
		imageList.clear();
	}
}