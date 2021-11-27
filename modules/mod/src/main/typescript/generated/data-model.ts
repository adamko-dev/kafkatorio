
// Generated code - do edit this file manually

interface FactorioObjectData {
    object_name: string;
}

interface FactorioEvent {
    data: FactorioObjectData;
    event_type: string;
    mod_version: string;
    tick: uint;
}

interface PositionData {
    x: int;
    y: int;
}

interface PlayerData extends FactorioObjectData {
    associated_character_unit_numbers: uint[];
    character_unit_number: uint;
    object_name: string;
    position_data: PositionData;
}

interface EntityData extends FactorioObjectData {
    active: boolean;
    health: float | null;
    health_ratio: float;
    name: string;
    object_name: string;
    player_index: uint | null;
    position: PositionData;
    surface_index: int;
    type: string;
    unit_number: uint | null;
}

interface SurfaceData extends FactorioObjectData {
    daytime: float;
    index: uint;
    name: string;
    object_name: string;
}