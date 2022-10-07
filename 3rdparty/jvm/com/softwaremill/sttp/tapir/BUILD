jvm_artifact(
    name="tapir-core",
    group="com.softwaremill.sttp.tapir",
    artifact="tapir-core_2.13",
    version="1.1.2",
    packages=["sttp.tapir.**"],
    excludes=[
        "com.softwaremill.sttp.model:core_2.13",
    ],
)

jvm_artifact(
    name="tapir-server",
    group="com.softwaremill.sttp.tapir",
    artifact="tapir-server_2.13",
    version="1.1.2",
    packages=["sttp.tapir.server.**"],
    excludes=[
        "com.softwaremill.sttp.tapir:tapir-core_2.13",
    ],
)

jvm_artifact(
    name="tapir-cats",
    group="com.softwaremill.sttp.tapir",
    artifact="tapir-cats_2.13",
    version="1.1.2",
    packages=["sttp.tapir.integ.cats.**"],
    excludes=[
        "com.softwaremill.sttp.tapir:tapir-core_2.13",
        "org.typelevel:cats-core_2.13",
        "org.typelevel:cats-effect_2.13",
    ],
)

jvm_artifact(
    name="tapir-json-circe",
    group="com.softwaremill.sttp.tapir",
    artifact="tapir-json-circe_2.13",
    version="1.1.2",
    packages=["sttp.tapir.json.circe.**"],
    excludes=[
        "com.softwaremill.sttp.tapir:tapir-core_2.13",
        "org.typelevel:cats-core_2.13",
        "org.typelevel:cats-effect_2.13",
    ],
)

jvm_artifact(
    name="tapir-http4s-server",
    group="com.softwaremill.sttp.tapir",
    artifact="tapir-http4s-server_2.13",
    version="1.1.2",
    packages=["sttp.tapir.server.http4s.**"],
    excludes=[
        "com.softwaremill.sttp.shared:fs2_2.13",
        "com.softwaremill.sttp.tapir:tapir-cats_2.13",
        "com.softwaremill.sttp.tapir:tapir-server_2.13",
        "org.http4s:http4s-server_2.13",
    ],
)



