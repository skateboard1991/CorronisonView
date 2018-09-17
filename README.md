# CorronisonView


![image](https://github.com/skateboard1991/CorronisonView/blob/master/show.gif)

使用很简单，在布局文件中直接使用CorronisonView

```
 <com.skateboard.corronisonview.CorronisonView
        app:duration="10"
        android:id="@+id/corronisonView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
其中duration代表了销毁时间。
然后在MainActivity里设置bitmap

```
corronisonView.setBitmap(BitmapFactory.decodeResource(resources,R.drawable.icon))
```
