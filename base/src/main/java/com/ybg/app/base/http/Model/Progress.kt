package com.ybg.app.base.http.Model

import java.io.Serializable

class Progress(val currentBytes: Long, val totalBytes: Long, val isFinish: Boolean) : Serializable
