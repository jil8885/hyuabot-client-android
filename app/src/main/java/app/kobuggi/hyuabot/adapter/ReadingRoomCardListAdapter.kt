package app.kobuggi.hyuabot.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.kobuggi.hyuabot.R
import app.kobuggi.hyuabot.model.*
import java.time.LocalDateTime

class ReadingRoomCardListAdapter(list: ArrayList<ReadingRoom>) : RecyclerView.Adapter<ReadingRoomCardListAdapter.ItemViewHolder>(){
    private val mList = list
    lateinit var mContext : Context

    inner class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        private val readingRoomCardTitle = itemView!!.findViewById<TextView>(R.id.reading_room_card_title)
        private val readingRoomCardSubTitle = itemView!!.findViewById<TextView>(R.id.reading_room_card_subtitle)
        private val readingRoomCardAll = itemView!!.findViewById<TextView>(R.id.reading_room_card_all)
        private val readingRoomCardAvailable = itemView!!.findViewById<TextView>(R.id.reading_room_card_available)
        private val readingRoomReserveButton = itemView!!.findViewById<ImageView>(R.id.reading_room_reserve_button)

        @SuppressLint("SetTextI18n")
        fun bind(item: ReadingRoom){
            readingRoomCardTitle.text = item.name
            readingRoomCardSubTitle.text = if (item.isReservable) "예약 중" else "예약 불가"
            readingRoomCardSubTitle.setTextColor(if (item.isReservable) Color.parseColor("#33a532") else Color.parseColor("#bb1e10"))
            readingRoomCardAll.text = "전체 좌석: ${item.activeTotal}석"
            readingRoomCardAvailable.text = "잔여 좌석: ${item.available}석"
            readingRoomReserveButton.setOnClickListener {
                if(item.isReservable){
                    Toast.makeText(mContext, "${item.name}의 잔여 좌석 알림을 예약했습니다.", Toast.LENGTH_SHORT).show()
                    readingRoomReserveButton.setImageResource(R.drawable.ic_alarm_on)
                } else{
                    Toast.makeText(mContext, "${item.name}은 예약이 불가능합니다.", Toast.LENGTH_SHORT).show()
                    readingRoomReserveButton.setImageResource(R.drawable.ic_alarm_off)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_reading_room, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }


}