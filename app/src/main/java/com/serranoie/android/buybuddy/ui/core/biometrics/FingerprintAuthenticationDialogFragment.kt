package com.serranoie.android.buybuddy.ui.core.biometrics

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.serranoie.android.buybuddy.R

@SuppressLint("RestrictedApi")
class FingerprintAuthenticationDialogFragment : AppCompatDialogFragment() {

    private lateinit var fingerprintManager: FingerprintManagerCompat
    private var cancellationSignal: CancellationSignal? = null
    private var authCallback: FingerprintAuthCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FingerprintAuthCallback) {
            authCallback = context
        } else {
            throw RuntimeException("$context must implement FingerprintAuthCallback")
        }
    }

    override fun onDetach() {
        super.onDetach()
        authCallback = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.AppCompatAlertDialogStyle
        )

        isCancelable = false
        fingerprintManager = FingerprintManagerCompat.from(requireContext())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogStyle)
            .setTitle(getString(R.string.bio_lock_title))
            .setMessage(getString(R.string.app_lock_setting_desc))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                cancellationSignal?.cancel()
            }
            .create()
    }

    override fun onResume() {
        super.onResume()
        startFingerprintAuthentication()
    }

    override fun onPause() {
        super.onPause()
        cancellationSignal?.cancel()
    }

    private fun startFingerprintAuthentication() {
        cancellationSignal = CancellationSignal()
        fingerprintManager.authenticate(
            null,
            0,
            cancellationSignal,
            object : FingerprintManagerCompat.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    dismiss()

                    Toast.makeText(
                        this@FingerprintAuthenticationDialogFragment.requireContext(),
                        getString(R.string.auth_error, errString),
                        Toast.LENGTH_LONG,
                    ).show()

                    authCallback?.onAuthenticationSucceeded(success = false)
                }

                override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult) {
                    dismiss()
                    authCallback?.onAuthenticationSucceeded(success = true)
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(
                        this@FingerprintAuthenticationDialogFragment.requireContext(),
                        getString(R.string.auth_failed),
                        Toast.LENGTH_LONG,
                    ).show()
                    authCallback?.onAuthenticationSucceeded(success = false)
                }
            },
            null
        )
    }
}
