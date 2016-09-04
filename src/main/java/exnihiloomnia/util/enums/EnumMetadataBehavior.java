package exnihiloomnia.util.enums;

import com.google.gson.annotations.SerializedName;

public enum EnumMetadataBehavior
{
	@SerializedName("meta_ignored")
	IGNORED, // Metadata is ignored.
	@SerializedName("meta_specific")
	SPECIFIC // Specific metadata value is required
}
