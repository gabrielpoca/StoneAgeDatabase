#!/usr/bin/ruby

FOLDER = "/Users/gabriel/Projects/StongeAgeDatabase/StoneAgeServer/out/production/StoneAgeServer"

# JAVA_PORTS = [{:client => 1099, :sync => 9999, :master => 1}, {:client => 2000, :sync => 9998, :master  => 0}]

Dir.chdir FOLDER
system("java stoneageserver.StoneAgeServer #{ARGV[0]} #{ARGV[1]} #{ARGV[2]}")