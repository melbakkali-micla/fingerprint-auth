// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "FingerprintAuth",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "FingerprintAuth",
            targets: ["FingerprintAuthPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "FingerprintAuthPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/FingerprintAuthPlugin"),
        .testTarget(
            name: "FingerprintAuthPluginTests",
            dependencies: ["FingerprintAuthPlugin"],
            path: "ios/Tests/FingerprintAuthPluginTests")
    ]
)