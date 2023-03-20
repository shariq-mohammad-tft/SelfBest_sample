package com.tft.selfbest.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tft.selfbest.R
import com.tft.selfbest.models.mycourse.AddCourse
import com.tft.selfbest.models.mycourse.SuggestedCourse

class SuggestedCourseAdapter(
    val list: List<SuggestedCourse>,
    private val addCourseListener: AddCourseListener
) :
    RecyclerView.Adapter<SuggestedCourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedCourseViewHolder {
        return SuggestedCourseViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.suggested_course_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SuggestedCourseViewHolder, position: Int) {
        val courseDetail = list[position]
        holder.loadImage(courseDetail.courseImageUrl)
        holder.courseName.text = courseDetail.courseName
        holder.sourceLogo.setImageResource(if (courseDetail.provider == "udemy") R.drawable.udemy_icon else R.drawable.youtube_icon)
        holder.modules.text = courseDetail.modules.toString()
        holder.durations.text = "${courseDetail.duration} Hours"
        holder.price.text = courseDetail.price.toString()
        holder.courseName.setOnClickListener(View.OnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            val testUrl = courseDetail.URL
            openURL.data = Uri.parse(testUrl)
            ContextCompat.startActivity(holder.view.context, openURL, null)
        })
        holder.addToCurrentCourse.setOnClickListener(View.OnClickListener {
            val addCourseObject = AddCourse(
                courseDetail.courseId.toString(),
                courseDetail.courseName,
                courseDetail.provider,
                courseDetail.URL,
                courseDetail.duration,
                courseDetail.courseImageUrl,
                courseDetail.modules
            )
            addCourseListener.getAddedCourse(addCourseObject)
            (list as ArrayList<SuggestedCourse>).removeAt(position)
            this.notifyItemRemoved(position)
        })
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    interface AddCourseListener {
        fun getAddedCourse(addCourse: AddCourse)
    }
}

class SuggestedCourseViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val courseImageUrl: ImageView = view.findViewById(R.id.course_icon)
    val courseName: TextView = view.findViewById(R.id.my_course_name)
    val sourceLogo: ImageView = view.findViewById(R.id.source_app_logo)
    val modules: TextView = view.findViewById(R.id.modules)
    val durations: TextView = view.findViewById(R.id.durations)
    val price: TextView = view.findViewById(R.id.price)
    val addToCurrentCourse: TextView = view.findViewById(R.id.add_course)
    fun loadImage(url: String) {
        Glide.with(view).load(url).into(courseImageUrl)
    }
}