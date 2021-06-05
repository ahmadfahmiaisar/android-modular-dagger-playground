package io.fajarca.project.post

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import io.fajarca.project.base.router.Routable
import kotlinx.android.parcel.Parcelize

class PostRouter {

    companion object : Routable<PostActivity, PostRouterData> {

        override val route: Class<PostActivity>
            get() = PostActivity::class.java
        override val routerDataClass: Class<PostRouterData>
            get() = PostRouterData::class.java
        override val deepLinkCode: Int
            get() = 1

        override fun startDeeplink(context: Context, data: Uri?) {
            val id  : Int = data?.getQueryParameter("id")?.toInt() ?: 0
            startActivity(context, PostRouterData(id))
        }


    }

}