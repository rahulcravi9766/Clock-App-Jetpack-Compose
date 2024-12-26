package com.example.composelearnings.utils

import org.w3c.dom.Text


fun Long.addZero(): String {
    return if (this <= 9) {
        "0$this"
    } else {
        this.toString()
    }
}