import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.ecotrack.android.R

class MarkerDetailsFragment : DialogFragment() {

    private var trashType: String? = null
    private var fillinglevel: Int? = null

    companion object {
        fun newInstance(trashType: String, fillinglevel: Int): MarkerDetailsFragment {
            val fragment = MarkerDetailsFragment()
            val args = Bundle()
            args.putString("trashType", trashType)
            args.putInt("fillinglevel", fillinglevel)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trashType = arguments?.getString("trashType")
        fillinglevel = arguments?.getInt("fillinglevel")
        setStyle(STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_marker_details, container, false)

        val trashTypeTextView: TextView = view.findViewById(R.id.trashTypeTextView)
        val fillingLevelTextView: TextView = view.findViewById(R.id.fillingLevelTextView)
        val closeButton: ImageView = view.findViewById(R.id.closeButton)

        trashTypeTextView.text = trashType
        fillingLevelTextView.text = "Filling Level: $fillinglevel%"

        closeButton.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels * 0.85).toInt(), // Set width to 85% of screen width
                ViewGroup.LayoutParams.WRAP_CONTENT // Height adjusts to content
            )
            clearFlags(android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND) // Remove background dim
        }
    }

}
