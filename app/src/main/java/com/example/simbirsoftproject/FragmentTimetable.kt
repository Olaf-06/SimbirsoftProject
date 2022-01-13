package com.example.simbirsoftproject

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*
import kotlin.collections.ArrayList

class MyEventDay(day: Calendar, imageResource: Int, var data: MutableList<Data>) : EventDay(day, imageResource)

class FragmentTimetable : Fragment(), View.OnClickListener, OnDayClickListener{

    private var events = ArrayList<EventDay>()
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val dataOfLessons: MutableList<Data> = ArrayList()
    private lateinit var myCalendarView: CalendarView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_timetable, container, false)

        dialog = Dialog(context!!)
        dialog.setContentView(R.layout.my_calendar_view)

        floatingActionButton = view.findViewById(R.id.fabTimetable) as FloatingActionButton
        floatingActionButton.setOnClickListener(this)

        myCalendarView = dialog.findViewById(R.id.myCalendarView) as CalendarView
        myCalendarView.setOnDayClickListener(this)

        recyclerView = view.findViewById(R.id.rvTimetable)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        db.collection("GroupLessons")
            .get()
            .addOnCompleteListener { p0 ->
                for (document: QueryDocumentSnapshot in Objects.requireNonNull(p0.result!!)) {
                    Log.d("logmy", "В цикле1")
                    val dataClass = document.toObject(Data::class.java)
                    dataOfLessons.add(
                        Data(
                            dataClass.name,
                            dataClass.description,
                            dataClass.photoID,
                            dataClass.startTime,
                            dataClass.endTime,
                            dataClass.day,
                            dataClass.month,
                            dataClass.year
                        )
                    )
                }
                setData(dataOfLessons)
            }

        return view
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.fabTimetable -> dialog.show()
        }
    }

    fun setData(dataOfLessons: MutableList<Data>){

        dataOfLessons.forEach {
            val list: MutableList<Data> = ArrayList()
            list.add(it)
            for(i in dataOfLessons.indices){
                if (it.year == dataOfLessons[i].year
                    && it.day == dataOfLessons[i].day
                    && it.month == dataOfLessons[i].month
                    && it != dataOfLessons[i]){
                    list.add(dataOfLessons[i])
                }
            }

            val calendar: Calendar = Calendar.getInstance()
            calendar.set(
                it.year.toInt(),
                it.month.toInt(),
                it.day.toInt())
            myCalendarView.setDate(calendar)
            events.add(MyEventDay(calendar, R.drawable.fui_ic_googleg_color_24dp, list))
        }

        myCalendarView.setEvents(events)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDayClick(eventDay: EventDay?) {

        eventDay as MyEventDay

        recyclerView.adapter = DataAdapterGroupLessons(this.context,
            eventDay.data as java.util.ArrayList<Data>)
        val adapter: DataAdapterGroupLessons = recyclerView.adapter as DataAdapterGroupLessons
        adapter.notifyDataSetChanged()
    }
}