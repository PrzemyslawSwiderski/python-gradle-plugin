rootProject.name = "multiModuleTestParent"

include(
    ":nestedModule",
    ":nestedModule:nestedNestedModule"
)
