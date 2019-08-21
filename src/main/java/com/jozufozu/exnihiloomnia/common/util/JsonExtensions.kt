package com.jozufozu.exnihiloomnia.common.util

import com.google.gson.JsonObject

operator fun JsonObject.contains(string: String) = this.has(string)
