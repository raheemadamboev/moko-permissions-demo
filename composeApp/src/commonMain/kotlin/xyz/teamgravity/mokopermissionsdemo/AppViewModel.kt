package xyz.teamgravity.mokopermissionsdemo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import kotlinx.coroutines.launch
import kotlin.time.TimeSource

class AppViewModel(
    private val controller: PermissionsController
) : ViewModel() {

    var state: PermissionState by mutableStateOf(PermissionState.NotDetermined)
        private set

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun onCheckPermission() {
        viewModelScope.launch {
            if (controller.isPermissionGranted(Permission.RECORD_AUDIO)) state = PermissionState.Granted
        }
    }

    fun onRequestRecordAudioPermission() {
        viewModelScope.launch {
            if (state == PermissionState.DeniedAlways) {
                controller.openAppSettings()
            } else {
                val time = TimeSource.Monotonic.markNow()
                try {
                    controller.providePermission(Permission.RECORD_AUDIO)
                    state = PermissionState.Granted
                } catch (e: DeniedAlwaysException) {
                    state = PermissionState.DeniedAlways
                    // 100 milliseconds enough time to know that permission was denied always, since dialog won't be shown
                    // i believe user can't deny the permission in less than 100 milliseconds
                    if (time.elapsedNow().inWholeMilliseconds < 100) onRequestRecordAudioPermission()
                } catch (e: DeniedException) {
                    state = PermissionState.Denied
                } catch (e: RequestCanceledException) {
                    e.printStackTrace()
                }
            }
        }
    }
}