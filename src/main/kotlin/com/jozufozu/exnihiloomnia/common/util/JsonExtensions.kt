package com.jozufozu.exnihiloomnia.common.util

import com.google.gson.JsonObject

operator fun JsonObject.contains(entry: String): Boolean = this.has(entry)