package com.tft.selfbest.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tft.selfbest.R
import com.tft.selfbest.databinding.AccessibiltyOnDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccessibilityOnDialog : DialogFragment(), View.OnClickListener {

    lateinit var binding: AccessibiltyOnDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.white_full_round_border)
        binding = AccessibiltyOnDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.cancel_event -> {
                dialog!!.dismiss()
            }

            R.id.save -> {
                //  if not construct intent to request permission
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                // request permission via start activity for result
                startActivity(intent)
                dialog!!.dismiss()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        binding.cancelEvent.setOnClickListener(this)
        binding.save.setOnClickListener(this)
    }
}