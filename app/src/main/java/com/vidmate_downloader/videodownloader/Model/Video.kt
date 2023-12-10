package com.vidmate_downloader.videodownloader.Model

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.vidmate_downloader.videodownloader.home
import java.io.File

data class Video(val id: String, var title: String, val duration: Long = 0, val folderName: String, val size: String
                 , var path: String, var artUri: Uri,var dateC: String)

data class Folder(val id: String, val folderName: String)

@SuppressLint("InlinedApi", "Recycle", "Range")
fun getAllVideos(context: Context): ArrayList<Video>{
    val sortEditor = context.getSharedPreferences("Sorting", AppCompatActivity.MODE_PRIVATE)
    home.sortValue = sortEditor.getInt("sortValue", 0)

    //for avoiding duplicate folders
    home.folderList = ArrayList()

    val tempList = ArrayList<Video>()
    val tempFolderList = ArrayList<String>()
    val projection = arrayOf(
        MediaStore.Video.Media.TITLE, MediaStore.Video.Media.SIZE, MediaStore.Video.Media._ID,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_ADDED,
        MediaStore.Video.Media.DURATION, MediaStore.Video.Media.BUCKET_ID)
    val cursor = context.contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null,
        home.sortList[home.sortValue])
    if(cursor != null)
        if(cursor.moveToNext())
            do {
                //checking null safety with ?: operator
                val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))?:"Unknown"
                val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))?:"Unknown"
                val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))?:"Internal Storage"
                val folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))?:"Unknown"
                val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))?:"0"
                val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))?:"Unknown"
                val dateC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))?:"Unknown"
                //just add null checking in end, this 0L is alternative value if below function returns a null value
                val durationC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))?.toLong()?:0L

                try {
                    val file = File(pathC)
                    val artUriC = Uri.fromFile(file)
                    val video = Video(title = titleC, id = idC, folderName = folderC, duration = durationC, size = sizeC,
                        path = pathC, artUri = artUriC, dateC = dateC)
                    if(file.exists()) tempList.add(video)

                    //for adding folders
                    if(!tempFolderList.contains(folderC) && !folderC.contains("Internal Storage")){
                        tempFolderList.add(folderC)
                        home.folderList.add(Folder(id = folderIdC, folderName = folderC))
                    }


                }catch (e:Exception){}
            }while (cursor.moveToNext())
    cursor?.close()
    return tempList
}

@SuppressLint("InlinedApi", "Recycle", "Range")
fun getAllAudios(context: Context): ArrayList<Video>{
    val sortEditor = context.getSharedPreferences("Sorting", AppCompatActivity.MODE_PRIVATE)
    home.sortValue = sortEditor.getInt("sortValue", 0)

    //for avoiding duplicate folders
    home.AudiofolderList = ArrayList()

    val tempList = ArrayList<Video>()
    val tempFolderList = ArrayList<String>()
    val projection = arrayOf(
        MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.BUCKET_DISPLAY_NAME, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DATE_ADDED,
        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.BUCKET_ID)
    val cursor = context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null,
        home.sortList[home.sortValue])
    if(cursor != null)
        if(cursor.moveToNext())
            do {
                //checking null safety with ?: operator
                val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))?:"Unknown"
                val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))?:"Unknown"
                val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME))?:"Internal Storage"
                val folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_ID))?:"Unknown"
                val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE))?:"0"
                val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))?:"Unknown"
                val dateC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED))?:"Unknown"
                //just add null checking in end, this 0L is alternative value if below function returns a null value
                val durationC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))?.toLong()?:0L

                try {
                    val file = File(pathC)
                    val artUriC = Uri.fromFile(file)
                    val video = Video(title = titleC, id = idC, folderName = folderC, duration = durationC, size = sizeC,
                        path = pathC, artUri = artUriC, dateC = dateC)
                    if(file.exists()) tempList.add(video)

                    //for adding folders
                    if(!tempFolderList.contains(folderC) && !folderC.contains("Internal Storage")){
                        tempFolderList.add(folderC)
                        home.AudiofolderList.add(Folder(id = folderIdC, folderName = folderC))
                    }
                }catch (e:Exception){}
            }while (cursor.moveToNext())
    cursor?.close()
    return tempList
}

@SuppressLint("InlinedApi", "Recycle", "Range")
fun getAllImages(context: Context): ArrayList<Video>{
    val sortEditor = context.getSharedPreferences("Sorting", AppCompatActivity.MODE_PRIVATE)
    home.sortValue = sortEditor.getInt("sortValue", 0)

    //for avoiding duplicate folders
    home.ImagefolderList = ArrayList()

    val tempList = ArrayList<Video>()
    val tempFolderList = ArrayList<String>()
    val projection = arrayOf(
        MediaStore.Images.Media.TITLE, MediaStore.Images.Media.SIZE, MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.DURATION, MediaStore.Images.Media.BUCKET_ID)
    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null,
        home.sortList[home.sortValue])
    if(cursor != null)
        if(cursor.moveToNext())
            do {
                //checking null safety with ?: operator
                val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE))?:"Unknown"
                val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID))?:"Unknown"
                val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))?:"Internal Storage"
                val folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))?:"Unknown"
                val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))?:"0"
                val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))?:"Unknown"
                val dateC = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED))?:"Unknown"
                //just add null checking in end, this 0L is alternative value if below function returns a null value
                val durationC = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DURATION))?.toLong()?:0L

                try {
                    val file = File(pathC)
                    val artUriC = Uri.fromFile(file)
                    val video = Video(title = titleC, id = idC, folderName = folderC, duration = durationC, size = sizeC,
                        path = pathC, artUri = artUriC, dateC = dateC)
                    if(file.exists()) tempList.add(video)

                    //for adding folders
                    if(!tempFolderList.contains(folderC) && !folderC.contains("Internal Storage")){
                        tempFolderList.add(folderC)
                        home.ImagefolderList.add(Folder(id = folderIdC, folderName = folderC))
                    }
                }catch (e:Exception){}
            }while (cursor.moveToNext())
    cursor?.close()
    return tempList
}

fun checkForInternet(context: Context): Boolean {

    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION") val networkInfo =
            connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}
