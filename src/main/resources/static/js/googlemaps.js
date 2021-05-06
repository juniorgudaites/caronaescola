var apiKey = 'AIzaSyD4bLmS8GiE2kWzazp3UUxRXEc8EZKbMYs';

function autoComplete(){
	var input = document.getElementById('endereco');
	autocomplete = new google.maps.places.Autocomplete(input);
}

function initMap() {
	var brasil = {
		lat : -23.5489,
		lng : -46.6388
	};

	var map = new google.maps.Map(document.getElementById('map'), {
		center : brasil,
		scrollwheel : true,
		zoom : 10
	});

	for (index = 0; index < alunos.length; ++index) {
		var latitude = alunos[index].contato.coordinates[0];
		var longitude = alunos[index].contato.coordinates[1];
		var coordenadas = {
			lat : latitude,
			lng : longitude
		};
		var marker = new google.maps.Marker({
			position : coordenadas,
			label: alunos[index].nome
		});
		marker.setMap(map);
	}
}
