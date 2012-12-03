#!/usr/bin/ruby

require 'readline'

class RMI
	attr_reader :ports, :folder, :threads
	def initialize(ports, folder)
		@ports = ports
		@folder = folder
		@threads = Array.new
	end
	def start
		@ports.each {|port| @threads.push Thread.new{self.rmiregistry(port)} }
	end
	def stop
		system("killall rmiregistry")
	end
	def rmiregistry(port)
		Dir.chdir @folder
		puts "rmiregistry on port #{port}"
		system("rmiregistry #{port}")
	end
end

class Server
	attr_reader :ports, :foder
	def initialize ports, folder
		@folder = folder
		@ports = ports
	end
	def start
		@ports.each {|entry| Thread.new{ self.run(entry[:client], entry[:sync], entry[:master]) } }
	end
	def stop
		system("killall java")
	end
	def run client, sync, master
		Dir.chdir @folder
		puts "peer on client #{client} sync #{sync} and master #{master}"
		system "java stoneageserver.StoneAgeServer #{client} #{sync} #{master}"
	end
end

FOLDER = "/Users/gabriel/Projects/StongeAgeDatabase/StoneAgeServer/out/production/StoneAgeServer"

RMI_PORTS = [1099, 2000]
rmi = RMI.new RMI_PORTS, FOLDER
rmi.start

JAVA_PORTS = [{:client => 1099, :sync => 9999, :master => 1}, {:client => 2000, :sync => 9998, :master  => 0}]
# server = Server.new JAVA_PORTS, FOLDER

begin
	while line = Readline.readline('> ', true)
	 	if line.eql?("restart")
	 		rmi.stop
	 		Thread.sleep 1000
	 		rmi.start
	 	elsif line.eql("server start")
	 		# server.start
	 	elsif line.eql("server stop")
	 		# server.stop
	 	elsif line.eql?("exit")
	 		rmi.stop
	 		exit
	 	else
	 		puts "unkown command"
	 	end
	end
rescue Interrupt => e
	rmi.stop
  	exit
end

# server.stop
rmi.stop