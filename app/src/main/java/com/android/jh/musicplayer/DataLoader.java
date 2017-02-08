package com.android.jh.musicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    // 데이터 컨테츠 URI 정의
    private final static Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    // 데이터에서 가져올 데이터 컬럼명을 String 배열에 담는다.
    // 데이터컬럼명은 Content Uri 의 패키지에 들어있다.
    private final static String PROJ[] = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST
    };
    // datas 를 두개의 activity에서 공유하기 위해 static 형태로 변경
    private static List<Music> datas = new ArrayList<>();

    // static 변수인 datas 를 체크해서 널이면 load 를 실행
    public static List<Music> get(Context context){
        if(datas == null || datas.size() == 0){
            load(context);
        }
        return datas;
    }

    // load 함수는 get 함수를 통해서만 접근한다.
    private static void load(Context context){
        // 1. 데이터에 접근하기위해 ContentResolver 를 불러온다.
        ContentResolver resolver = context.getContentResolver();

        // 4. Content Resolver 로 쿼리한 데이터를 Cursor 에 담는다.
        Cursor cursor = resolver.query(URI, PROJ, null, null, null);

        // 5. Cursor 에 담긴 데이터를 반복문을 돌면서 꺼낸다
        if(cursor != null){
            while(cursor.moveToNext()){
                Music music = new Music();
                // 데이터
                music.id = getValue(cursor,PROJ[0]);
                music.album_id = getValue(cursor,PROJ[1]);
                music.title = getValue(cursor,PROJ[2]);
                music.artist = getValue(cursor,PROJ[3]);
                music.album_image = getAlbumImageSimple(music.album_id);
                music.uri = getMusicUri(music.id);

                // 주석처리...
                // music.bitmap_image = getAlbumImageBitmap(music.album_id);
                //
                datas.add(music);
            }
            // 6. 처리 후 커서를 닫아준다
            cursor.close();
        }
    }

    private static String getValue(Cursor cursor,String columnName) {
        int idx = cursor.getColumnIndex(columnName);
        return cursor.getString(idx);
    }


    private static Uri getMusicUri(String music_id) {

        Uri content_uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        return Uri.withAppendedPath(content_uri,music_id);

    }
    // 앨범 Uri 생성
    private static Uri getAlbumImageSimple(String album_id){
        return Uri.parse("content://media/external/audio/albumart/" + album_id);
    }


//
//    @Deprecated
//    private static Bitmap getAlbumImageBitmap(Context context, String album_id){
//        // 1. 앨범아이디로 Uri 생성
//        Uri uri = getAlbumImageSimple(album_id);
//        // 2. 컨텐트 리졸버 가져오기
//        ContentResolver resolver = context.getContentResolver();
//        try {
//            // 3. 리졸버에서 스트림열기
//            InputStream is = resolver.openInputStream(uri);
//            // 4. BitmapFactory 를 통해 이미지 데이터를 가져온다
//            Bitmap image = BitmapFactory.decodeStream(is);
//
//            // 5. 가져온 이미지를 리턴한다
//            return image;
//        }catch(FileNotFoundException e){
//            e.printStackTrace();
//        }
//        return null;
//    }
}
