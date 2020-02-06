#!/bin/bash

javac Clue.java GPSCoordinate.java TreasureHunt.java User.java && jar cfv TreasureHunt.jar Clue.class GPSCoordinate.class TreasureHunt.class User.class