package exnihiloomnia.registries.barrel.pojos;

public class BarrelCraftingRecipe {
	private String fluid;
	private String input;
	private int inputMeta = 0;
	private String output;
	private int outputMeta = 0;

	public BarrelCraftingRecipe() {}

	public BarrelCraftingRecipe(String fluid, String input, int inputMeta, String output, int outputMeta) {
		this.fluid = fluid;
		this.input = input;
		this.inputMeta = inputMeta;
		this.output = output;
		this.outputMeta = outputMeta;
	}

	public BarrelCraftingRecipe(String fluid, String input, String output) {
		this.fluid = fluid;
		this.input = input;
		this.output = output;
	}

	public String getFluid() {
		return fluid;
	}

	public String getInput() {
		return input;
	}

	public int getInputMeta() {
		return inputMeta;
	}

	public String getOutput() {
		return output;
	}

	public int getOutputMeta() {
		return outputMeta;
	}
}
