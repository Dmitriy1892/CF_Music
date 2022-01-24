package com.coldfier.cfmusic.ui.player_fragment

import com.coldfier.cfmusic.ui.base.BaseViewModel
import javax.inject.Inject

class PlayerViewModel @Inject constructor(): BaseViewModel() {

    var inc = 0
    fun getHello(): String = "Hello ${inc++}"
}