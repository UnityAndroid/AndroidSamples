package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "LyricalBit";
    private final static int DATABASE_VERSION = 3;
    public static SQLiteDatabase db;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS category(Id INTEGER,Cat_Name TEXT,Cat_img TEXT,page INTEGER,Back_img TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS theme(Id INTEGER,Cat_Id INTEGER,App_Version TEXT,Theme_Name TEXT,Thumnail_Big TEXT,Thumnail_Small TEXT,SoundFile TEXT,sound_size TEXT,GameobjectName TEXT,Is_Preimum TEXT,Theme_Counter TEXT,isNewRealise TEXT,Theme_Id INTEGER,counter INTEGER,lyrics TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS ftoken(id INTEGER PRIMARY KEY AUTOINCREMENT,androidid TEXT,token TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS themedownloads(themeid INTEGER,sound TEXT,image TEXT,tname TEXT,oname TEXT,iurl TEXT,surl TEXT,lyrics TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS ctheme(themeid INTEGER,sound TEXT,image TEXT,tname TEXT,oname TEXT,Theme_Id INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS cropimages(id INTEGER PRIMARY KEY AUTOINCREMENT,ipath TEXT,cpath TEXT,status INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS creations(themeid INTEGER,tname TEXT,filepath TEXT,id INTEGER PRIMARY KEY AUTOINCREMENT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS particlecategory(category_id INTEGER,category_name TEXT,icon_url TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS particle(id INTEGER,category_id INTEGER,theme_name TEXT,prefab_name TEXT,theme_url TEXT,thumb_url TEXT,particle_url TEXT,is_lock INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS particledownloads(id INTEGER,category_id INTEGER,theme_name TEXT,prefab_name TEXT,thumb TEXT,particle TEXT,thumb_url TEXT,particle_url TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS sortparticle(id INTEGER,particle_id INTEGER,theme_name TEXT,position INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS ads(id INTEGR,appid TEXT,banner TEXT,inter TEXT,reward TEXT,gnative TEXT,fbanner TEXT,finter TEXT,fnative TEXT,gstatus INTEGER,fstatus INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS anims(id INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS moreApps(Id INTEGER,application TEXT,app_link TEXT,icon TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS slider(id INTEGER,title TEXT,type TEXT,icon TEXT,name TEXT,status TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS category");
        db.execSQL("DROP TABLE IF EXISTS theme");

        db.execSQL("DROP TABLE IF EXISTS ftoken");

        db.execSQL("DROP TABLE IF EXISTS ctheme");
        db.execSQL("DROP TABLE IF EXISTS cropimages");

        db.execSQL("DROP TABLE IF EXISTS particlecategory");
        db.execSQL("DROP TABLE IF EXISTS particle");
        db.execSQL("DROP TABLE IF EXISTS sortparticle");

        db.execSQL("DROP TABLE IF EXISTS ads");
        db.execSQL("DROP TABLE IF EXISTS anims");

        db.execSQL("DROP TABLE IF EXISTS moreApps");
        db.execSQL("DROP TABLE IF EXISTS slider");
        onCreate(db);
    }

    public void checkdb() {
        if (db == null) {
            db = getWritableDatabase();
        }
    }


    // Insert Data
    public void insertcatagorytable(int Id, String Cat_Name, String Cat_img, int page, String Back_img) {
        checkdb();
        db.execSQL("insert into category values(" + Id + ",'" + Cat_Name + "','" + Cat_img + "'," + page + ",'" + Back_img + "')");
    }

    public void insertthemetable(int Id, int Cat_Id, String App_Version, String Theme_Name, String Thumnail_Big, String Thumnail_Small, String SoundFile, String sound_size, String GameobjectName, String Is_Preimum, String Theme_Counter, String isNewRealise, int Theme_Id, int counter, String lyrics) {
        checkdb();
        db.execSQL("insert into theme values(" + Id + "," + Cat_Id + ",'" + App_Version + "','" + Theme_Name + "','" + Thumnail_Big + "','" + Thumnail_Small + "','" + SoundFile + "','" + sound_size + "','" + GameobjectName + "','" + Is_Preimum + "','" + Theme_Counter + "','" + isNewRealise + "'," + Theme_Id + "," + counter + ",'" + lyrics + "')");
    }

    public void inserttoken(String android_id, String token) {
        checkdb();
        db.execSQL("delete from ftoken");
        db.execSQL("insert into ftoken(androidid,token) values('" + android_id + "','" + token + "')");
    }

    public void insertSortParticle(int Id, int pid, String tname, int pos) {
        checkdb();
        db.execSQL("insert into sortparticle values(" + Id + "," + pid + ",'" + tname + "'," + pos + ")");
    }

    public void setcurrenttheme(int themeid, String sound, String image, String tname, String oname, int Theme_Id) {
        checkdb();
        db.execSQL("delete from ctheme");
        db.execSQL("insert into ctheme values(" + themeid + ",'" + sound + "','" + image + "','" + tname + "','" + oname + "'," + Theme_Id + ")");
    }

    public void insertdownloads(int themeid, String sound, String image, String tname, String oname, String iurl, String surl, String lyrics) {
        checkdb();
        db.execSQL("delete from themedownloads where themeid=" + themeid);
        db.execSQL("insert into themedownloads values(" + themeid + ",'" + sound + "','" + image + "','" + tname + "','" + oname + "','" + iurl + "','" + surl + "','" + lyrics + "')");
    }

    public void insertparticledownloads(int themeid, int cat_id, String theme_name, String prefab_name, String thumb, String particle, String turl, String purl) {
        checkdb();
        db.execSQL("delete from particledownloads where id=" + themeid);
        db.execSQL("insert into particledownloads values(" + themeid + "," + cat_id + ",'" + theme_name + "','" + prefab_name + "','" + thumb + "','" + particle + "','" + turl + "','" + purl + "')");
    }

    public void insertcreation(int themeid, String tname, String filepath) {
        checkdb();
        db.execSQL("insert into creations(themeid,tname,filepath) values(" + themeid + ",'" + tname + "','" + filepath + "')");
    }

    public void insertParticlecatagorytable(int Id, String Cat_Name, String icon) {
        checkdb();
        db.execSQL("insert into particlecategory values(" + Id + ",'" + Cat_Name + "','" + icon + "')");
    }

    public void insertparticle(int id, int Cat_Id, String theme_name, String prefab_name, String theme_url, String thumb_url, String particle_url, int is_lock) {
        checkdb();
        db.execSQL("insert into particle VALUES(" + id + "," + Cat_Id + ",'" + theme_name + "','" + prefab_name + "','" + theme_url + "','" + thumb_url + "','" + particle_url + "'," + is_lock + ")");
    }


    public void insertcropimages(String ipath, String cpath, int status) {
        checkdb();
        db.execSQL("insert into cropimages(ipath,cpath,status) values('" + ipath + "','" + cpath + "'," + status + ")");
    }

    public void insertads(int id, String appid, String banner, String inter, String reward, String gnative, String fbanner, String finter, String fnative, int gstatus, int fstatus) {
        checkdb();
        removeadstable();
        db.execSQL("insert into ads(id,appid,banner,inter,reward,gnative,fbanner,finter,fnative,gstatus,fstatus) values(" + id + ",'" + appid + "','" + banner + "','" + inter + "','" + reward + "','" + gnative + "','" + fbanner + "','" + finter + "','" + fnative + "'," + gstatus + "," + fstatus + ")");
    }

    public void insertanims(int pos) {
        checkdb();
        if (!checkanims(pos)) {
            db.execSQL("insert into anims VALUES(" + pos + ")");
        }
    }

    public boolean checkanims(int pos) {
        checkdb();
        boolean check = false;
        Cursor data = db.rawQuery("select * from anims where id=" + pos, null);
        if (data.moveToFirst()) {
            check = true;
        } else {
            check = false;
        }
        if (data != null)
            data.close();
        return check;
    }

    public void removeanims() {
        checkdb();
        db.execSQL("delete from anims");
    }


    // Delete Data

    public void removecatagorytable() {
        checkdb();
        db.execSQL("delete from category");
    }

    public void removethemetable() {
        checkdb();
        db.execSQL("delete from theme");
    }

    public void removecropimages() {
        checkdb();
        db.execSQL("delete from cropimages");
    }

    public void removelastcropimages() {
        checkdb();
        db.execSQL("delete from cropimages where id in (select max(id) from cropimages)");
    }

    public void removeselectedcropimages(int did) {
        checkdb();
        db.execSQL("delete from cropimages where id=" + did);
    }


    public void removeparticlecatagorytable() {
        checkdb();
        db.execSQL("delete from particlecategory");
    }

    public void removeparticletable() {
        checkdb();
        db.execSQL("delete from particle");
    }

    public void removesortparticletable() {
        checkdb();
        db.execSQL("delete from sortparticle");
    }

    public void deletecreation(String filepath) {
        checkdb();
        db.execSQL("delete from creations where filepath like '" + filepath + "'");
    }

    public void removeadstable() {
        checkdb();
        db.execSQL("delete from ads");
    }


    // Change Data

    public void updatecropimages(int uid, String croppath) {
        checkdb();
        db.execSQL("update cropimages set cpath='" + croppath + "' where id=" + uid + "");
    }

    public void updateParticleDownload(int id) {
        checkdb();
        db.execSQL("update particledownloads set particle_url='' where id=" + id);
    }

    public void updateParticleThumbnailDownload(int id, String imgPath) {
        checkdb();
        db.execSQL("update particledownloads set thumb='" + imgPath + "',particle_url='' where id=" + id);
    }

    // Fetch Data

    public Cursor featchCatagarytable() {
        checkdb();
        return db.rawQuery("select * from category", null);
    }

    public Cursor featchThemetable(int Cat_Id) {
        checkdb();
        return db.rawQuery("select * from theme where Cat_Id=" + Cat_Id, null);
    }

    public Cursor getcreationbyfilepath(String file) {
        checkdb();
        return db.rawQuery("select * from creations where filepath like '" + file + "'", null);
    }

    public Cursor featchparticleCatagarytable() {
        checkdb();
        return db.rawQuery("select * from particlecategory", null);
    }

    public Cursor featchParticletable() {
        checkdb();
        return db.rawQuery("select * from particle", null);
    }

    public Cursor featchParticleById(int id) {
        checkdb();
        return db.rawQuery("select * from particle where id =" + id, null);
    }

    public Cursor featchSortParticle() {
        checkdb();
        return db.rawQuery("select * from sortparticle", null);
    }

    public Cursor featchParticletable(int Cat_Id) {
        checkdb();
        return db.rawQuery("select * from particle where category_id=" + Cat_Id, null);
    }


    public Cursor getparticleCategorybyid(int catid) {
        checkdb();
        return db.rawQuery("select * from particlecategory where category_id=" + catid, null);
    }

    public Cursor getallparticleCategory() {
        checkdb();
        return db.rawQuery("select * from particlecategory", null);
    }

    public Cursor getalldownloadedparticlebycategoryid(int catid) {
        checkdb();
        return db.rawQuery("select * from particledownloads where category_id=" + catid, null);
    }

    public Cursor getdownloadedparticlebyid(int catid) {
        checkdb();
        return db.rawQuery("select * from particledownloads where id=" + catid, null);
    }

    public Cursor getcreation() {
        checkdb();
        return db.rawQuery("select * from creations order by id desc", null);
    }

    public Cursor getdownloads(int themeid) {
        checkdb();
        return db.rawQuery("select * from themedownloads where themeid=" + themeid, null);
    }

    public Cursor getparticledownloads(int themeid) {
        checkdb();
        return db.rawQuery("select * from particledownloads where id=" + themeid, null);
    }

    public Cursor getthemebyid(int id) {
        checkdb();
        return db.rawQuery("select * from theme where Id=" + id, null);
    }

    public Cursor getcurrenttheme() {
        checkdb();
        return db.rawQuery("select * from ctheme", null);
    }

    public Cursor getcropimages() {
        checkdb();
        return db.rawQuery("select * from cropimages order by id asc", null);
    }

    public String gettoken() {
        checkdb();
        String check = "";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from ftoken", null);
            if (cursor.moveToFirst()) {
                check = cursor.getString(cursor.getColumnIndex("token"));
            } else {
                check = "";
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return check;
    }

    // Check Data

    public boolean checktheme(int themeid) {
        checkdb();
        boolean check = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from themedownloads where themeid=" + themeid, null);
            if (cursor.moveToFirst()) {
                check = true;
            } else {
                check = false;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return check;
    }

    public boolean checkthemeimageurl(int themeid, String url) {
        checkdb();
        boolean check = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from themedownloads where themeid = " + themeid + " AND iurl like '" + url + "'", null);
            if (cursor.moveToFirst()) {
                check = true;
            } else {
                check = false;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return check;
    }


    public boolean checkthemesongurl(int themeid, String url) {
        checkdb();
        boolean check = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from themedownloads where themeid = " + themeid + " AND surl like '" + url + "'", null);
            if (cursor.moveToFirst()) {
                check = true;
            } else {
                check = false;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return check;
    }

    public boolean checkthemesongurl(int themeid, String soundUrl, String imageUrl) {
        checkdb();
        boolean check = false;
        Cursor data = db.rawQuery("select * from themedownloads where themeid = " + themeid + " AND surl like '" + soundUrl + "'" + " AND iurl like '" + imageUrl + "'", null);
        if (data.moveToFirst()) {
            check = true;
        } else {
            check = false;
        }
        if (data != null)
            data.close();
        return check;
    }

    public boolean checkcropimages(String image) {
        checkdb();
        boolean check = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from cropimages where ipath  like '" + image + "'", null);
            if (cursor.moveToFirst()) {
                check = true;
            } else {
                check = false;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return check;

    }

    public int checkcropimageposition(String image) {
        checkdb();
        int check = -1;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from cropimages", null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(cursor.getColumnIndex("ipath")).equalsIgnoreCase(image)) {
                        check = cursor.getPosition();
                        check = check + 1;
                        break;
                    }
                } while (cursor.moveToNext());
            } else {
                check = -1;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return check;

    }


    public boolean checkparticle(int themeid) {
        checkdb();
        boolean check = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from particledownloads where id=" + themeid, null);
            if (cursor.moveToFirst()) {
                check = true;
            } else {
                check = false;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return check;
    }

    public boolean checktparticlethumburl(int themeid, String url) {
        checkdb();
        boolean check = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from particledownloads where id = " + themeid + " AND thumb_url like '" + url + "'", null);
            if (cursor.moveToFirst()) {
                check = true;
            } else {
                check = false;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return check;
    }


    public boolean checkparticleurl(int themeid, String url) {
        checkdb();
        boolean check = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from particledownloads where id = " + themeid + " AND particle_url like '" + url + "'", null);
            if (cursor.moveToFirst()) {
                check = true;
            } else {
                check = false;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return check;

    }

    public boolean checkcropedpimages(int id) {

        checkdb();
        boolean check = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from cropimages where id=" + id, null);
            if (cursor.moveToFirst()) {
                check = true;
            } else {
                check = false;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return check;
    }

    public void insertSlider(int id, String title, String type, String icon, String name, String status) {
        checkdb();
        db.execSQL("insert into slider VALUES(" + id + ",'" + title + "','" + type + "','" + icon + "','" + name + "','" + status + "')");
    }

    public Cursor fetchSlider() {
        checkdb();
        Cursor cursor = db.rawQuery("select * from slider", null);
        return cursor;
    }

    public void removeSlider() {
        checkdb();
        db.execSQL("delete from slider");
    }

    public int getpage(int cat_id) {
        checkdb();
        int check = 0;
        Cursor data = db.rawQuery("select * from category where Id=" + cat_id, null);
        if (data.moveToFirst()) {
            check = data.getInt(data.getColumnIndex("page"));
        } else {
            check = 1;
        }
        if (data != null)
            data.close();
        return check;
    }

    public void increpage(int catid) {
        checkdb();
        int pagecount = getpage(catid) + 1;
        db.execSQL("update category set page = " + pagecount + " where Id=" + catid);
    }

    public void removeAddMoreTable() {
        checkdb();
        db.execSQL("delete from moreApps");
    }

    public void insertAddMoreTable(int Id, String application, String app_link, String icon) {
        checkdb();
        db.execSQL("insert into moreApps values(" + Id + ",'" + application + "','" + app_link + "','" + icon + "')");
    }

    public Cursor fetchAddMore() {
        checkdb();
        Cursor cursor = db.rawQuery("select * from moreApps", null);
        return cursor;
    }

    public Cursor getThemes() {
        checkdb();
        Cursor cursor = db.rawQuery("SELECT * FROM theme", null);
        return cursor;
    }
}
