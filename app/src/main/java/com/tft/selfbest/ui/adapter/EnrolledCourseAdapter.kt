package com.tft.selfbest.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tft.selfbest.R
import com.tft.selfbest.models.mycourse.EnrolledCourse

class EnrolledCourseAdapter(
    val list: List<EnrolledCourse>,
    val uploadCertificateListener: UploadCertificateListener
) :
    RecyclerView.Adapter<EnrolledCourseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrolledCourseViewHolder {
        return EnrolledCourseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.course_detail_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EnrolledCourseViewHolder, position: Int) {
        val courseDetail = list[position]
        holder.loadImage(courseDetail.courseImageUrl)
        holder.courseName.text = courseDetail.courseName
        val progressStatus = ((courseDetail.modulesCompleted / courseDetail.modules) * 100)
        holder.progressBar.progress = progressStatus
        holder.sourceLogo.setImageResource(if (courseDetail.type == "udemy") R.drawable.udemy_icon else R.drawable.youtube_icon)
        holder.modules.text = "${courseDetail.modulesCompleted} of ${courseDetail.modules}"
        holder.durations.text = "${courseDetail.duration} Hours"
        holder.courseName.setOnClickListener(View.OnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            val testUrl = courseDetail.courseUrl
            openURL.data = Uri.parse(testUrl)
            startActivity(holder.view.context, openURL, null)
        })
        if (courseDetail.certificateStatus == "Pending") {
            holder.uploadCertificate.setBackgroundResource(R.color.orange)
            holder.certificateStatus.text = "Pending"
            holder.uploadCertificateIcon.setImageResource(R.drawable.arror_drop_up)
            holder.certificateStatus.setTextColor(
                ContextCompat.getColor(
                    holder.view.context,
                    R.color.white
                )
            )
        } else if (courseDetail.modulesCompleted == courseDetail.modules) {
            holder.uploadCertificate.setBackgroundResource(R.color.tool_bar_color)
            holder.certificateStatus.setTextColor(
                ContextCompat.getColor(
                    holder.view.context,
                    R.color.white
                )
            )
            holder.uploadCertificateIcon.setImageResource(R.drawable.white_upload_certificate_icon)
            holder.uploadCertificate.setOnClickListener(View.OnClickListener {
                uploadCertificateListener.uploadCourseCertificate(courseDetail.courseId)
                holder.uploadCertificate.setBackgroundResource(R.color.orange)
                holder.certificateStatus.text = "Pending"
                holder.uploadCertificateIcon.setImageResource(R.drawable.arror_drop_up)
                holder.certificateStatus.setTextColor(
                    ContextCompat.getColor(
                        holder.view.context,
                        R.color.white
                    )
                )
            })
        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    interface UploadCertificateListener {
        fun uploadCourseCertificate(courseId: String)
    }
}

class EnrolledCourseViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val courseImageUrl: ImageView = view.findViewById(R.id.course_icon)
    val courseName: TextView = view.findViewById(R.id.my_course_name)
    val sourceLogo: ImageView = view.findViewById(R.id.source_app_logo)
    val modules: TextView = view.findViewById(R.id.modules)
    val durations: TextView = view.findViewById(R.id.durations)
    val progressBar: ProgressBar = view.findViewById(R.id.course_progress_bar)
    val uploadCertificate: LinearLayout = view.findViewById(R.id.certificates_container)
    val uploadCertificateIcon: ImageView = view.findViewById(R.id.upload_icon)
    val certificateStatus: TextView = view.findViewById(R.id.certificates_status)
    fun loadImage(url: String) {
        Glide.with(view).load(url).into(courseImageUrl)
    }
}
