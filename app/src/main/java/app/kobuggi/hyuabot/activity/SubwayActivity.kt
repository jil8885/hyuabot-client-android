package app.kobuggi.hyuabot.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.kobuggi.hyuabot.BuildConfig
import app.kobuggi.hyuabot.R
import app.kobuggi.hyuabot.adapter.BusCardListAdapter
import app.kobuggi.hyuabot.adapter.SubwayCardListAdapter
import app.kobuggi.hyuabot.config.AppServerService
import app.kobuggi.hyuabot.function.getDarkMode
import app.kobuggi.hyuabot.model.*
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class SubwayActivity : AppCompatActivity() {
    // 네트워크 클라이언트
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.server_url)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val appServerService = retrofit.create(AppServerService::class.java)
    private var subwayCardListAdapter: SubwayCardListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subway)

        // 광고 로드
        loadNativeAd()

        val toolbar = findViewById<Toolbar>(R.id.subway_app_bar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        fetchSubwayDepartureInfo()
    }

    private fun loadNativeAd(){
        val builder = AdLoader.Builder(this, BuildConfig.admob_unit_id)
        val config = this.resources.configuration
        builder.forNativeAd{
            val template = findViewById<TemplateView>(R.id.subway_admob_template)
            val bgColor = ColorDrawable(if(getDarkMode(config)) Color.BLACK else Color.WHITE)
            val textColor = if(getDarkMode(config)) Color.WHITE else Color.BLACK
            val templateStyle = NativeTemplateStyle.Builder()
                .withMainBackgroundColor(bgColor)
                .withPrimaryTextTypefaceColor(textColor)
                .withSecondaryTextTypefaceColor(textColor)
                .build()
            template.setStyles(templateStyle)
            template.setNativeAd(it)
        }
        val adLoader = builder.build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun fetchSubwayDepartureInfo() = Observable.interval(0, 1, TimeUnit.MINUTES)
        .subscribe{
            val request = appServerService.getSubwayERICA(CampusRequest("erica"))
            request.enqueue(object : Callback<SubwayERICA> {
                override fun onResponse(call: Call<SubwayERICA>, response: Response<SubwayERICA>) {
                    if(response.isSuccessful && response.body() != null){
                        if(subwayCardListAdapter == null){
                            initSubwayDepartureInfo(response.body()!!)
                        } else {
                            updateSubwayDepartureInfo(response.body()!!)
                        }
                    }
                }

                override fun onFailure(call: Call<SubwayERICA>, t: Throwable) {

                }
            })
        }

    private fun initSubwayDepartureInfo(subwayDepartureInfo : SubwayERICA){
        val subwayDepartureData = arrayListOf(
            SubwayCardItem(getString(R.string.subway_line_4), R.drawable.subway_stop_circle_line_4, getString(R.string.heading_to_4_seoul), subwayDepartureInfo.line4.realtime.upLine, subwayDepartureInfo.line4.timetable.upLine),
            SubwayCardItem(getString(R.string.subway_line_4), R.drawable.subway_stop_circle_line_4, getString(R.string.heading_to_4_oido), subwayDepartureInfo.line4.realtime.downLine, subwayDepartureInfo.line4.timetable.downLine),
            SubwayCardItem(getString(R.string.subway_line_suin), R.drawable.subway_stop_circle_line_suin, getString(R.string.heading_to_suin_seoul), subwayDepartureInfo.lineSuin.realtime.upLine, subwayDepartureInfo.lineSuin.timetable.upLine),
            SubwayCardItem(getString(R.string.subway_line_suin), R.drawable.subway_stop_circle_line_suin, getString(R.string.heading_to_suin_incheon), subwayDepartureInfo.lineSuin.realtime.downLine, subwayDepartureInfo.lineSuin.timetable.downLine)
        )
        subwayCardListAdapter = SubwayCardListAdapter(subwayDepartureData, this)
        val subwayCardListView = findViewById<RecyclerView>(R.id.subway_card_list_view)
        subwayCardListView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        subwayCardListView.adapter = subwayCardListAdapter
    }

    private fun updateSubwayDepartureInfo(subwayDepartureInfo : SubwayERICA){
        val subwayDepartureData = arrayListOf(
            SubwayCardItem(getString(R.string.subway_line_4), R.drawable.subway_stop_circle_line_4, getString(R.string.heading_to_4_seoul), subwayDepartureInfo.line4.realtime.upLine, subwayDepartureInfo.line4.timetable.upLine),
            SubwayCardItem(getString(R.string.subway_line_4), R.drawable.subway_stop_circle_line_4, getString(R.string.heading_to_4_oido), subwayDepartureInfo.line4.realtime.downLine, subwayDepartureInfo.line4.timetable.downLine),
            SubwayCardItem(getString(R.string.subway_line_suin), R.drawable.subway_stop_circle_line_suin, getString(R.string.heading_to_suin_seoul), subwayDepartureInfo.lineSuin.realtime.upLine, subwayDepartureInfo.lineSuin.timetable.upLine),
            SubwayCardItem(getString(R.string.subway_line_suin), R.drawable.subway_stop_circle_line_suin, getString(R.string.heading_to_suin_incheon), subwayDepartureInfo.lineSuin.realtime.downLine, subwayDepartureInfo.lineSuin.timetable.downLine)
        )
        subwayCardListAdapter!!.notifyItemRangeChanged(0, 4, subwayDepartureData)
    }
}