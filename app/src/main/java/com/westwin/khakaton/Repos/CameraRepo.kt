package com.westwin.khakaton.Repos

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.westwin.khakaton.CameraContract
import com.android.volley.toolbox.Volley
import com.westwin.khakaton.App
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.NetworkResponse
import com.android.volley.VolleyLog
import com.android.volley.AuthFailureError
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class CameraRepo : CameraContract.Repository {

    override fun postImg(
        url: String,
        name: String?,
        img: String?,
        sync: CameraContract.Repository.CameraSync
    ) {
        val queue = Volley.newRequestQueue(App.getContext())

        val request = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                if (response.isNotEmpty()) sync.sendEmotion(JSONObject(response).get("emotion").toString())
            },
            Response.ErrorListener { error ->
                sync.onError(error.message!!)
            }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray? {
                try {
                    return img?.toByteArray(Charsets.UTF_8)
                } catch (uee: UnsupportedEncodingException) {
                    VolleyLog.wtf(
                        "Unsupported Encoding while trying to get the bytes of %s using %s",
                        img,
                        "utf-8"
                    )
                    return null
                }
            }

            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["name"] = name!!
                return super.getHeaders()
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                var responseString = ""
                if (response != null) {
                    responseString = response
                        .statusCode
                        .toString()
                    // can get more details such as response.headers
                }
                return Response.success(
                    responseString,
                    HttpHeaderParser.parseCacheHeaders(response)
                )
            }
        }

        queue.add(request)
    }
}