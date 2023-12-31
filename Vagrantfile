### Configuration parameters ###
# Virtual machines to initialize count
VM_COUNT = 5

Vagrant.configure("2") do |config|

    config.ssh.insert_key = false

	(1..VM_COUNT).each do |i|

		config.vm.define "server#{i}" do |web|
			web.vm.box = "ubuntu/focal64"
			web.vm.network "forwarded_port", id: "ssh", host: 2221 + i, guest: 22
			web.vm.network "private_network", ip: "10.11.10.#{i + 1}", virtualbox__intnet: true
			if i == 1
			    web.vm.network "forwarded_port", id: "backend", host: 8080, guest: 8080
			    web.vm.network "forwarded_port", id: "frontend", host: 8081, guest: 80      # linux restricts insecure non-root connections to ports < 1024
			    web.vm.network "forwarded_port", id: "rmq", host: 15672, guest: 15672
			end
			if i == 5
			    web.vm.network "forwarded_port", id: "mysql", host: 3308, guest: 3306
			end
			web.vm.hostname = "server#{i}"

			web.vm.provider "virtualbox" do |v|
			    v.name = "server#{i}"
			    v.memory = 2048
			    v.cpus = 4
			end

			web.vm.provision "shell" do |s|
			    ssh_pub_key = File.readlines("#{Dir.home}/.ssh/id_rsa.pub").first.strip
			    s.inline = <<-SHELL
			    echo #{ssh_pub_key} >> /home/vagrant/.ssh/authorized_keys
			    echo #{ssh_pub_key} >> /root/.ssh/authorized_keys
			    SHELL
			end

			if i == VM_COUNT
			    web.vm.provision :ansible do |ansible|
			        ansible.playbook = "playbook.yml"
			        ansible.inventory_path = "inventory/cluster"
			        ansible.vault_password_file = ".vault_pass"
			        ansible.limit = "all"
			        # ansible.verbose = "v"
			    end
			end

	    end

	end

end

# ansible-playbook -i inventory --private-key=~/.vagrant.d/insecure_private_key -u vagrant dev.yml --tags="nginx, php"