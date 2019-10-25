package com.jozufozu.exnihiloomnia.common.blocks.leaves

import com.google.common.collect.HashBiMap
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor
import net.minecraft.block.material.PushReaction
import net.minecraft.client.particle.ParticleManager
import net.minecraft.entity.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.IFluidState
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.item.BlockItemUseContext
import net.minecraft.item.DyeColor
import net.minecraft.item.ItemStack
import net.minecraft.pathfinding.PathNodeType
import net.minecraft.pathfinding.PathType
import net.minecraft.state.StateContainer
import net.minecraft.state.StateHolder
import net.minecraft.tags.Tag
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.*
import net.minecraft.world.server.ServerWorld
import net.minecraft.world.storage.loot.LootContext
import net.minecraftforge.common.IPlantable
import net.minecraftforge.common.ToolType
import java.util.*
import java.util.function.Predicate

class MimicStateContainer(mimic: Block, block: Block)
    : StateContainer<Block, BlockState>(block, { block, _ ->
    val state = currentMimic.get().stateContainer.validStates[stateIndex++]
    val mimic = MimicBlockState(block, state)
    val ingToEd = tempMimickingToMimicked.get() ?: HashBiMap.create<BlockState, BlockState>().also { tempMimickingToMimicked.set(it) }
    val edToIng = tempMimickedToMimicking.get() ?: HashBiMap.create<BlockState, BlockState>().also { tempMimickedToMimicking.set(it) }
    edToIng[state] = mimic
    ingToEd[mimic] = state
    mimic as StateHolder<Block, BlockState>
}, mimic.stateContainer.properties.map { it.getName() to it }.toMap()) {

    private val mimickedToMimicking: HashBiMap<BlockState, BlockState>
    private val mimickingToMimicked: HashBiMap<BlockState, BlockState>

    init {
        mimickedToMimicking = tempMimickedToMimicking.get() ?: HashBiMap.create()
        mimickingToMimicked = tempMimickingToMimicked.get() ?: HashBiMap.create()

        tempMimickedToMimicking.set(null)
        tempMimickingToMimicked.set(null)
    }

    fun getMimickedBlockState(mimickingState: BlockState): BlockState = mimickingToMimicked[mimickingState] ?: throw BlockMimicException("Cannot map $mimickingState to it's normal state in ${this.owner.registryName}")
    fun getMimickingBlockState(stateToMimic: BlockState): BlockState = mimickedToMimicking[stateToMimic] ?: throw BlockMimicException("Cannot map $stateToMimic to it's mimicked state in ${this.owner.registryName}")

    companion object {
        // Oh boy is this hackey
        var currentMimic = ThreadLocal<Block>().also { it.set(Blocks.AIR) }
        var tempMimickedToMimicking = ThreadLocal<HashBiMap<BlockState, BlockState>?>()
        var tempMimickingToMimicked = ThreadLocal<HashBiMap<BlockState, BlockState>?>()
    }

    protected class MimicBlockState constructor(actual: Block, val mimic: BlockState) : BlockState(actual, mimic.values) {
        override fun getPickBlock(target: RayTraceResult?, world: IBlockReader?, pos: BlockPos?, player: PlayerEntity?): ItemStack {
            return mimic.getPickBlock(target, world, pos, player)
        }

        override fun getMaterial(): Material {
            return mimic.getMaterial()
        }

        override fun isValidPosition(worldIn: IWorldReader, pos: BlockPos): Boolean {
            return mimic.isValidPosition(worldIn, pos)
        }

        override fun addLandingEffects(worldserver: ServerWorld?, pos: BlockPos?, state2: BlockState?, entity: LivingEntity?, numberOfParticles: Int): Boolean {
            return mimic.addLandingEffects(worldserver, pos, state2, entity, numberOfParticles)
        }

        override fun isIn(tagIn: Tag<Block>): Boolean {
            return mimic.isIn(tagIn)
        }

        override fun getPositionRandom(pos: BlockPos): Long {
            return mimic.getPositionRandom(pos)
        }

        override fun onProjectileCollision(worldIn: World, state: BlockState, hit: BlockRayTraceResult, projectile: Entity) {
            mimic.onProjectileCollision(worldIn, state, hit, projectile)
        }

        override fun updateNeighbors(worldIn: IWorld, pos: BlockPos, flags: Int) {
            mimic.updateNeighbors(worldIn, pos, flags)
        }

        override fun getSoundType(): SoundType {
            return mimic.getSoundType()
        }

        override fun getSoundType(world: IWorldReader?, pos: BlockPos?, entity: Entity?): SoundType {
            return mimic.getSoundType(world, pos, entity)
        }

        override fun getOffset(access: IBlockReader, pos: BlockPos): Vec3d {
            return mimic.getOffset(access, pos)
        }

        override fun collisionExtendsVertically(world: IBlockReader?, pos: BlockPos?, collidingEntity: Entity?): Boolean {
            return mimic.collisionExtendsVertically(world, pos, collidingEntity)
        }

        override fun removedByPlayer(world: World?, pos: BlockPos?, player: PlayerEntity?, willHarvest: Boolean, fluid: IFluidState?): Boolean {
            return mimic.removedByPlayer(world, pos, player, willHarvest, fluid)
        }

        override fun isReplaceableOreGen(world: IWorldReader?, pos: BlockPos?, target: Predicate<BlockState>?): Boolean {
            return mimic.isReplaceableOreGen(world, pos, target)
        }

        override fun onBlockExploded(world: World?, pos: BlockPos?, explosion: Explosion?) {
            mimic.onBlockExploded(world, pos, explosion)
        }

        override fun isSideInvisible(state: BlockState, face: Direction): Boolean {
            return mimic.isSideInvisible(state, face)
        }

        override fun isBurning(world: IBlockReader?, pos: BlockPos?): Boolean {
            return mimic.isBurning(world, pos)
        }

        override fun canEntitySpawn(worldIn: IBlockReader, pos: BlockPos, type: EntityType<*>): Boolean {
            return mimic.canEntitySpawn(worldIn, pos, type)
        }

        override fun getRaytraceShape(worldIn: IBlockReader, pos: BlockPos): VoxelShape {
            return mimic.getRaytraceShape(worldIn, pos)
        }

        override fun canHarvestBlock(world: IBlockReader?, pos: BlockPos?, player: PlayerEntity?): Boolean {
            return mimic.canHarvestBlock(world, pos, player)
        }

        override fun getExplosionResistance(world: IWorldReader?, pos: BlockPos?, exploder: Entity?, explosion: Explosion?): Float {
            return mimic.getExplosionResistance(world, pos, exploder, explosion)
        }

        override fun onBlockAdded(worldIn: World, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {
            mimic.onBlockAdded(worldIn, pos, oldState, isMoving)
        }

        override fun getDrops(builder: LootContext.Builder): MutableList<ItemStack> {
            return mimic.getDrops(builder)
        }

        override fun isToolEffective(tool: ToolType?): Boolean {
            return mimic.isToolEffective(tool)
        }

        override fun allowsMovement(worldIn: IBlockReader, pos: BlockPos, type: PathType): Boolean {
            return mimic.allowsMovement(worldIn, pos, type)
        }

        override fun getBedDirection(world: IWorldReader?, pos: BlockPos?): Direction {
            return mimic.getBedDirection(world, pos)
        }

        override fun hasCustomBreakingProgress(): Boolean {
            return mimic.hasCustomBreakingProgress()
        }

        override fun canProvidePower(): Boolean {
            return mimic.canProvidePower()
        }

        override fun onEntityCollision(worldIn: World, pos: BlockPos, entityIn: Entity) {
            mimic.onEntityCollision(worldIn, pos, entityIn)
        }

        override fun isBeaconBase(world: IWorldReader?, pos: BlockPos?, beacon: BlockPos?): Boolean {
            return mimic.isBeaconBase(world, pos, beacon)
        }

        override fun hasTileEntity(): Boolean {
            return mimic.hasTileEntity()
        }

        override fun func_215704_f(): Boolean {
            return mimic.func_215704_f()
        }

        override fun ticksRandomly(): Boolean {
            return mimic.ticksRandomly()
        }

        override fun observedNeighborChange(world: World?, pos: BlockPos?, changed: Block?, changedPos: BlockPos?) {
            mimic.observedNeighborChange(world, pos, changed, changedPos)
        }

        override fun addRunningEffects(world: World?, pos: BlockPos?, entity: Entity?): Boolean {
            return mimic.addRunningEffects(world, pos, entity)
        }

        override fun getFogColor(world: IWorldReader?, pos: BlockPos?, entity: Entity?, originalColor: Vec3d?, partialTicks: Float): Vec3d {
            return mimic.getFogColor(world, pos, entity, originalColor, partialTicks)
        }

        override fun onNeighborChange(world: IWorldReader?, pos: BlockPos?, neighbor: BlockPos?) {
            mimic.onNeighborChange(world, pos, neighbor)
        }

        override fun spawnAdditionalDrops(worldIn: World, pos: BlockPos, stack: ItemStack) {
            mimic.spawnAdditionalDrops(worldIn, pos, stack)
        }

        override fun getLightValue(): Int {
            return mimic.getLightValue()
        }

        override fun getLightValue(world: IEnviromentBlockReader?, pos: BlockPos?): Int {
            return mimic.getLightValue(world, pos)
        }

        override fun getStateForPlacement(facing: Direction?, state2: BlockState?, world: IWorld?, pos1: BlockPos?, pos2: BlockPos?, hand: Hand?): BlockState {
            return mimic.getStateForPlacement(facing, state2, world, pos1, pos2, hand)
        }

        override fun isFlammable(world: IBlockReader?, pos: BlockPos?, face: Direction?): Boolean {
            return mimic.isFlammable(world, pos, face)
        }

        override fun func_224755_d(p_224755_1_: IBlockReader, p_224755_2_: BlockPos, p_224755_3_: Direction): Boolean {
            return mimic.func_224755_d(p_224755_1_, p_224755_2_, p_224755_3_)
        }

        override fun setBedOccupied(world: IWorldReader?, pos: BlockPos?, sleeper: LivingEntity?, occupied: Boolean) {
            mimic.setBedOccupied(world, pos, sleeper, occupied)
        }

        override fun isBed(world: IBlockReader?, pos: BlockPos?, player: LivingEntity?): Boolean {
            return mimic.isBed(world, pos, player)
        }

        override fun isOpaqueCube(worldIn: IBlockReader, pos: BlockPos): Boolean {
            return mimic.isOpaqueCube(worldIn, pos)
        }

        override fun getShape(worldIn: IBlockReader, pos: BlockPos): VoxelShape {
            return mimic.getShape(worldIn, pos)
        }

        override fun getShape(worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
            return mimic.getShape(worldIn, pos, context)
        }

        override fun getRenderShape(worldIn: IBlockReader, pos: BlockPos): VoxelShape {
            return mimic.getRenderShape(worldIn, pos)
        }

        override fun canConnectRedstone(world: IBlockReader?, pos: BlockPos?, side: Direction?): Boolean {
            return mimic.canConnectRedstone(world, pos, side)
        }

        override fun isLadder(world: IWorldReader?, pos: BlockPos?, entity: LivingEntity?): Boolean {
            return mimic.isLadder(world, pos, entity)
        }

        override fun beginLeaveDecay(world: IWorldReader?, pos: BlockPos?) {
            mimic.beginLeaveDecay(world, pos)
        }

        override fun onBlockEventReceived(worldIn: World, pos: BlockPos, id: Int, param: Int): Boolean {
            return mimic.onBlockEventReceived(worldIn, pos, id, param)
        }

        override fun getExtendedState(world: IBlockReader?, pos: BlockPos?): BlockState {
            return mimic.getExtendedState(world, pos)
        }

        override fun canBeReplacedByLogs(world: IWorldReader?, pos: BlockPos?): Boolean {
            return mimic.canBeReplacedByLogs(world, pos)
        }

        override fun updatePostPlacement(face: Direction, queried: BlockState, worldIn: IWorld, currentPos: BlockPos, offsetPos: BlockPos): BlockState {
            return mimic.updatePostPlacement(face, queried, worldIn, currentPos, offsetPos)
        }

        override fun canSustainPlant(world: IBlockReader?, pos: BlockPos?, facing: Direction?, plantable: IPlantable?): Boolean {
            return mimic.canSustainPlant(world, pos, facing, plantable)
        }

        override fun func_224756_o(p_224756_1_: IBlockReader, p_224756_2_: BlockPos): Boolean {
            return mimic.func_224756_o(p_224756_1_, p_224756_2_)
        }

        override fun getStrongPower(blockAccess: IBlockReader, pos: BlockPos, side: Direction): Int {
            return mimic.getStrongPower(blockAccess, pos, side)
        }

        override fun getFluidState(): IFluidState {
            return mimic.getFluidState()
        }

        override fun doesSideBlockRendering(world: IEnviromentBlockReader?, pos: BlockPos?, face: Direction?): Boolean {
            return mimic.doesSideBlockRendering(world, pos, face)
        }

        override fun addDestroyEffects(world: World?, pos: BlockPos?, manager: ParticleManager?): Boolean {
            return mimic.addDestroyEffects(world, pos, manager)
        }

        override fun getContainer(worldIn: World, pos: BlockPos): INamedContainerProvider? {
            return mimic.getContainer(worldIn, pos)
        }

        override fun getBlock(): Block {
            return mimic.getBlock()
        }

        override fun canRenderInLayer(layer: BlockRenderLayer?): Boolean {
            return mimic.canRenderInLayer(layer)
        }

        override fun canEntityDestroy(world: IBlockReader?, pos: BlockPos?, entity: Entity?): Boolean {
            return mimic.canEntityDestroy(world, pos, entity)
        }

        override fun getHarvestLevel(): Int {
            return mimic.getHarvestLevel()
        }

        override fun canBeReplacedByLeaves(world: IWorldReader?, pos: BlockPos?): Boolean {
            return mimic.canBeReplacedByLeaves(world, pos)
        }

        override fun rotate(rot: Rotation): BlockState {
            return mimic.rotate(rot)
        }

        override fun rotate(world: IWorld?, pos: BlockPos?, direction: Rotation?): BlockState {
            return mimic.rotate(world, pos, direction)
        }

        override fun getPlayerRelativeBlockHardness(player: PlayerEntity, worldIn: IBlockReader, pos: BlockPos): Float {
            return mimic.getPlayerRelativeBlockHardness(player, worldIn, pos)
        }

        override fun onPlantGrow(world: IWorld?, pos: BlockPos?, source: BlockPos?) {
            mimic.onPlantGrow(world, pos, source)
        }

        override fun getBlockHardness(worldIn: IBlockReader, pos: BlockPos): Float {
            return mimic.getBlockHardness(worldIn, pos)
        }

        override fun propagatesSkylightDown(worldIn: IBlockReader, pos: BlockPos): Boolean {
            return mimic.propagatesSkylightDown(worldIn, pos)
        }

        override fun getCollisionShape(worldIn: IBlockReader, pos: BlockPos): VoxelShape {
            return mimic.getCollisionShape(worldIn, pos)
        }

        override fun getCollisionShape(worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
            return mimic.getCollisionShape(worldIn, pos, context)
        }

        override fun tick(worldIn: World, pos: BlockPos, random: Random) {
            mimic.tick(worldIn, pos, random)
        }

        override fun mirror(mirrorIn: Mirror): BlockState {
            return mimic.mirror(mirrorIn)
        }

        override fun func_215703_d(reader: IBlockReader, pos: BlockPos): Float {
            return mimic.func_215703_d(reader, pos)
        }

        override fun isFireSource(world: IBlockReader?, pos: BlockPos?, side: Direction?): Boolean {
            return mimic.isFireSource(world, pos, side)
        }

        override fun causesSuffocation(worldIn: IBlockReader, pos: BlockPos): Boolean {
            return mimic.causesSuffocation(worldIn, pos)
        }

        override fun canBeConnectedTo(world: IBlockReader?, pos: BlockPos?, facing: Direction?): Boolean {
            return mimic.canBeConnectedTo(world, pos, facing)
        }

        override fun shouldCheckWeakPower(world: IWorldReader?, pos: BlockPos?, side: Direction?): Boolean {
            return mimic.shouldCheckWeakPower(world, pos, side)
        }

        override fun getOpacity(worldIn: IBlockReader, pos: BlockPos): Int {
            return mimic.getOpacity(worldIn, pos)
        }

        override fun randomTick(worldIn: World, pos: BlockPos, random: Random) {
            mimic.randomTick(worldIn, pos, random)
        }

        override fun getPushReaction(): PushReaction {
            return mimic.getPushReaction()
        }

        override fun isFoliage(world: IWorldReader?, pos: BlockPos?): Boolean {
            return mimic.isFoliage(world, pos)
        }

        override fun onReplaced(worldIn: World, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
            mimic.onReplaced(worldIn, pos, newState, isMoving)
        }

        override fun getEnchantPowerBonus(world: IWorldReader?, pos: BlockPos?): Float {
            return mimic.getEnchantPowerBonus(world, pos)
        }

        override fun createTileEntity(world: IBlockReader?): TileEntity? {
            return mimic.createTileEntity(world)
        }

        override fun getMaterialColor(worldIn: IBlockReader, pos: BlockPos): MaterialColor {
            return mimic.getMaterialColor(worldIn, pos)
        }

        override fun getFireSpreadSpeed(world: IBlockReader?, pos: BlockPos?, face: Direction?): Int {
            return mimic.getFireSpreadSpeed(world, pos, face)
        }

        override fun getValidRotations(world: IBlockReader?, pos: BlockPos?): Array<Direction>? {
            return mimic.getValidRotations(world, pos)
        }

        override fun blockNeedsPostProcessing(worldIn: IBlockReader, pos: BlockPos): Boolean {
            return mimic.blockNeedsPostProcessing(worldIn, pos)
        }

        override fun canDropFromExplosion(world: IBlockReader?, pos: BlockPos?, explosion: Explosion?): Boolean {
            return mimic.canDropFromExplosion(world, pos, explosion)
        }

        override fun getFlammability(world: IBlockReader?, pos: BlockPos?, face: Direction?): Int {
            return mimic.getFlammability(world, pos, face)
        }

        override fun getWeakChanges(world: IWorldReader?, pos: BlockPos?): Boolean {
            return mimic.getWeakChanges(world, pos)
        }

        override fun func_215692_c() {
            mimic.func_215692_c()
        }

        override fun recolorBlock(world: IWorld?, pos: BlockPos?, facing: Direction?, color: DyeColor?): Boolean {
            return mimic.recolorBlock(world, pos, facing, color)
        }

        override fun isSolid(): Boolean {
            return mimic.isSolid()
        }

        override fun isFertile(world: IBlockReader?, pos: BlockPos?): Boolean {
            return mimic.isFertile(world, pos)
        }

        override fun getBlockState(): BlockState {
            return mimic.getBlockState()
        }

        override fun hasComparatorInputOverride(): Boolean {
            return mimic.hasComparatorInputOverride()
        }

        override fun getComparatorInputOverride(worldIn: World, pos: BlockPos): Int {
            return mimic.getComparatorInputOverride(worldIn, pos)
        }

        override fun updateDiagonalNeighbors(worldIn: IWorld, pos: BlockPos, flags: Int) {
            mimic.updateDiagonalNeighbors(worldIn, pos, flags)
        }

        override fun getStateAtViewpoint(world: IBlockReader?, pos: BlockPos?, viewpoint: Vec3d?): BlockState {
            return mimic.getStateAtViewpoint(world, pos, viewpoint)
        }

        override fun onBlockActivated(worldIn: World, player: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult): Boolean {
            return mimic.onBlockActivated(worldIn, player, handIn, hit)
        }

        override fun isReplaceable(useContext: BlockItemUseContext): Boolean {
            return mimic.isReplaceable(useContext)
        }

        override fun getAiPathNodeType(world: IBlockReader?, pos: BlockPos?, entity: MobEntity?): PathNodeType? {
            return mimic.getAiPathNodeType(world, pos, entity)
        }

        override fun addHitEffects(world: World?, target: RayTraceResult?, manager: ParticleManager?): Boolean {
            return mimic.addHitEffects(world, target, manager)
        }

        override fun getBeaconColorMultiplier(world: IWorldReader?, pos: BlockPos?, beacon: BlockPos?): FloatArray? {
            return mimic.getBeaconColorMultiplier(world, pos, beacon)
        }

        override fun isAir(): Boolean {
            return mimic.isAir()
        }

        override fun isAir(world: IBlockReader?, pos: BlockPos?): Boolean {
            return mimic.isAir(world, pos)
        }

        override fun func_215702_a(worldIn: IBlockReader, pos: BlockPos, directionIn: Direction): VoxelShape {
            return mimic.func_215702_a(worldIn, pos, directionIn)
        }

        override fun getRenderType(): BlockRenderType {
            return mimic.getRenderType()
        }

        override fun getWeakPower(blockAccess: IBlockReader, pos: BlockPos, side: Direction): Int {
            return mimic.getWeakPower(blockAccess, pos, side)
        }

        override fun isNormalCube(reader: IBlockReader, pos: BlockPos): Boolean {
            return mimic.isNormalCube(reader, pos)
        }

        override fun getExpDrop(world: IWorldReader?, pos: BlockPos?, fortune: Int, silktouch: Int): Int {
            return mimic.getExpDrop(world, pos, fortune, silktouch)
        }

        override fun isBedFoot(world: IWorldReader?, pos: BlockPos?): Boolean {
            return mimic.isBedFoot(world, pos)
        }

        override fun func_215691_g(): Boolean {
            return mimic.func_215691_g()
        }

        override fun neighborChanged(worldIn: World, p_215697_2_: BlockPos, blockIn: Block, p_215697_4_: BlockPos, isMoving: Boolean) {
            mimic.neighborChanged(worldIn, p_215697_2_, blockIn, p_215697_4_, isMoving)
        }

        override fun onBlockClicked(worldIn: World, pos: BlockPos, player: PlayerEntity) {
            mimic.onBlockClicked(worldIn, pos, player)
        }

        override fun getSlipperiness(world: IWorldReader?, pos: BlockPos?, entity: Entity?): Float {
            return mimic.getSlipperiness(world, pos, entity)
        }

        override fun getPackedLightmapCoords(reader: IEnviromentBlockReader, pos: BlockPos): Int {
            return mimic.getPackedLightmapCoords(reader, pos)
        }

        override fun canCreatureSpawn(world: IWorldReader?, pos: BlockPos?, type: EntitySpawnPlacementRegistry.PlacementType?, entityType: EntityType<*>?): Boolean {
            return mimic.canCreatureSpawn(world, pos, type, entityType)
        }

        override fun getHarvestTool(): ToolType {
            return mimic.getHarvestTool()
        }

        override fun getBedSpawnPosition(type: EntityType<*>?, world: IWorldReader?, pos: BlockPos?, sleeper: LivingEntity?): Optional<Vec3d> {
            return mimic.getBedSpawnPosition(type, world, pos, sleeper)
        }

        override fun isStickyBlock(): Boolean {
            return mimic.isStickyBlock()
        }
    }
}