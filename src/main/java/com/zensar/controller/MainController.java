package com.zensar.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zensar.model.Aircraft;
import com.zensar.model.Airport;
import com.zensar.model.Flight;
import com.zensar.model.Passenger;
import com.zensar.model.Payment;
import com.zensar.services.AircraftService;
import com.zensar.services.AirportService;
import com.zensar.services.FlightService;
import com.zensar.services.PassengerService;

@Controller
public class MainController {

	@Autowired
	AirportService airportService;
	@Autowired
	AircraftService aircraftService;
	@Autowired
	FlightService flightService;
	@Autowired
	PassengerService passengerService;
	@Autowired
	HttpSession session;
	@Autowired
	PaymentService paymentService;

	@GetMapping("/")
	public String showHomePage() {
		return "index";
	}

	@GetMapping("/airport/new")
	public String showAddAirportPage(Model model) {
		model.addAttribute("airport", new Airport());
		return "newAirport";
	}

	@PostMapping("/airport/new")
	public String saveAirport(@Valid @ModelAttribute("airport") Airport airport, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("airport", new Airport());
			return "newAirport";
		}
		airportService.saveAirport(airport);
		model.addAttribute("airports", airportService.getAllAirportsPaged(0));
		model.addAttribute("currentPage", 0);
		return "airports";
	}

	@GetMapping("/airport/delete")
	public String deleteAirport(@PathParam("airportId") int airportId, Model model) {
		airportService.deleteAirport(airportId);
		model.addAttribute("airports", airportService.getAllAirportsPaged(0));
		model.addAttribute("currentPage", 0);
		return "airports";
	}

	@GetMapping("/airports")
	public String showAirportsList(@RequestParam(defaultValue = "0") int pageNo, Model model) {
		model.addAttribute("airports", airportService.getAllAirportsPaged(pageNo));
		model.addAttribute("currentPage", pageNo);
		return "airports";
	}

	@GetMapping("/aircraft/new")
	public String showAddAircraftPage(Model model) {
		model.addAttribute("aircraft", new Aircraft());
		return "newAircraft";
	}

	@PostMapping("/aircraft/new")
	public String saveAircraft(@Valid @ModelAttribute("aircraft") Aircraft aircraft, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("aircraft", new Aircraft());
			return "newAircraft";
		}
		aircraftService.saveAircraft(aircraft);
		model.addAttribute("aircrafts", aircraftService.getAllAircraftsPaged(0));
		model.addAttribute("currentPage", 0);
		return "aircrafts";
	}

	@GetMapping("/aircraft/delete")
	public String deleteAircraft(@PathParam("aircraftId") long aircraftId, Model model) {
		aircraftService.deleteAircraftById(aircraftId);
		model.addAttribute("aircrafts", aircraftService.getAllAircraftsPaged(0));
		model.addAttribute("currentPage", 0);
		return "aircrafts";
	}

	@GetMapping("/aircrafts")
	public String showAircraftsList(@RequestParam(defaultValue = "0") int pageNo, Model model) {
		model.addAttribute("aircrafts", aircraftService.getAllAircraftsPaged(pageNo));
		model.addAttribute("currentPage", pageNo);
		return "aircrafts";
	}

	@GetMapping("/flight/new")
	public String showNewFlightPage(Model model) {
		model.addAttribute("flight", new Flight());
		model.addAttribute("aircrafts", aircraftService.getAllAircrafts());
		model.addAttribute("airports", airportService.getAllAirports());
		return "newFlight";
	}

	@PostMapping("/flight/new")
	public String saveFlight(@Valid @ModelAttribute("flight") Flight flight, BindingResult bindingResult,
			@RequestParam("departureAirport") int departureAirport,
			@RequestParam("destinationAirport") int destinationAirport, @RequestParam("aircraft") long aircraftId,
			@RequestParam("arrivalTime") String arrivalTime, @RequestParam("departureTime") String departureTime,
			Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("flight", new Flight());
			model.addAttribute("aircrafts", aircraftService.getAllAircrafts());
			model.addAttribute("airports", airportService.getAllAirports());
			return "newFlight";
		}
		if (departureAirport == destinationAirport) {
			model.addAttribute("sameAirportError", "Departure and destination airport can't be same");
			model.addAttribute("flight", new Flight());
			model.addAttribute("aircrafts", aircraftService.getAllAircrafts());
			model.addAttribute("airports", airportService.getAllAirports());
			return "newFlight";
		}

		flight.setAircraft(aircraftService.getAircraftById(aircraftId));
		flight.setDepartureAirport(airportService.getAirportById(departureAirport));
		flight.setDestinationAirport(airportService.getAirportById(destinationAirport));
		flight.setDepartureTime(departureTime);
		flight.setArrivalTime(arrivalTime);
		flightService.saveFlight(flight);

		model.addAttribute("flights", flightService.getAllFlightsPaged(0));
		model.addAttribute("currentPage", 0);
		return "flights";
	}

	@GetMapping("/flight/delete")
	public String deleteFlight(@PathParam("flightId") long flightId, Model model) {
		flightService.deleteFlightById(flightId);
		model.addAttribute("flights", flightService.getAllFlightsPaged(0));
		model.addAttribute("currentPage", 0);
		return "flights";
	}

	@GetMapping("/flights")
	public String showFlightsList(@RequestParam(defaultValue = "0") int pageNo, Model model) {
		model.addAttribute("flights", flightService.getAllFlightsPaged(pageNo));
		model.addAttribute("currentPage", pageNo);
		return "flights";
	}

	@GetMapping("/flight/search")
	public String showSearchFlightPage(Model model) {
		model.addAttribute("airports", airportService.getAllAirports());
		model.addAttribute("flights", null);
		return "searchFlight";
	}

	@PostMapping("/flight/search")
	public String searchFlight(@RequestParam("departureAirport") int departureAirport,
			@RequestParam("destinationAirport") int destinationAirport,
			@RequestParam("departureTime") String departureTime, Model model) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate deptTime = LocalDate.parse(departureTime, dtf);
		Airport depAirport = airportService.getAirportById(departureAirport);
		Airport destAirport = airportService.getAirportById(destinationAirport);

		if (departureAirport == destinationAirport) {
			model.addAttribute("AirportError", "Departure and destination airport cant be same!");
			model.addAttribute("airports", airportService.getAllAirports());
			return "searchFlight";
		}
		List<Flight> flights = flightService.getAllFlightsByAirportAndDepartureTime(depAirport, destAirport, deptTime);
		if (flights.isEmpty()) {
			model.addAttribute("notFound", "No Record Found!");
		} else {
			model.addAttribute("flights", flights);
		}

		model.addAttribute("airports", airportService.getAllAirports());
		return "searchFlight";
	}

	@GetMapping("/flight/book")
	public String showBookFlightPage(Model model) {
		model.addAttribute("airports", airportService.getAllAirports());
		return "bookFlight";
	}

	@PostMapping("/flight/book")
	public String searchFlightToBook(@RequestParam("departureAirport") int departureAirport,
			@RequestParam("destinationAirport") int destinationAirport,
			@RequestParam("departureTime") String departureTime, Model model) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate deptTime = LocalDate.parse(departureTime, dtf);
		Airport depAirport = airportService.getAirportById(departureAirport);
		Airport destAirport = airportService.getAirportById(destinationAirport);

		if (departureAirport == destinationAirport) {
			model.addAttribute("AirportError", "Departure and destination airport cant be same!");
			model.addAttribute("airports", airportService.getAllAirports());
			return "bookFlight";
		}
		List<Flight> flights = flightService.getAllFlightsByAirportAndDepartureTime(depAirport, destAirport, deptTime);
		if (flights.isEmpty()) {
			model.addAttribute("notFound", "No Record Found!");
		} else {
			model.addAttribute("flights", flights);
		}
		model.addAttribute("airports", airportService.getAllAirports());
		return "bookFlight";
	}

	@GetMapping("/flight/book/new")
	public String showCustomerInfoPage(@RequestParam long flightId, Model model) {
		model.addAttribute("flightId", flightId);
		model.addAttribute("passenger", new Passenger());
		return "newPassenger";
	}

	@PostMapping("/flight/book/new")
	public String bookFlight(@Valid @ModelAttribute("passenger") Passenger passenger, BindingResult bindingResult,
			@RequestParam("flightId") long flightId, Model model) {
		Flight flight = flightService.getFlightById(flightId);
		Passenger passenger1 = passenger;
		passenger1.setFlight(flight);
		//passengerService.savePassenger(passenger1);
		session.putValue("passenger", passenger1);
		model.addAttribute("flight", flight);
		return "payment";
	}

	@GetMapping("/flight/book/verify")
	public String showVerifyBookingPage() {
		return "verifyBooking";
	}

	@PostMapping("/flight/book/verify")
	public String showVerifyBookingPageResult(@RequestParam("flightId") long flightId,
			@RequestParam("passengerId") long passengerId, Model model) {

		Flight flight = flightService.getFlightById(flightId);
		if (flight != null) {
			model.addAttribute("flight", flight);
			List<Passenger> passengers = flight.getPassengers();
			Passenger passenger = null;
			for (Passenger p : passengers) {
				if (p.getPassengerId() == passengerId) {
					passenger = passengerService.getPassengerById(passengerId);
					model.addAttribute("passenger", passenger);
				}
			}
			if (passenger != null) {
				return "verifyBooking";
			} else {
				model.addAttribute("notFound", "Not Found");
				return "verifyBooking";
			}
		} else {
			model.addAttribute("notFound", "Not Found");
			return "verifyBooking";
		}
	}

	@PostMapping("/flight/book/cancel")
	public String cancelTicket(@RequestParam("passengerId") long passengerId, Model model) {
		passengerService.deletePassengerById(passengerId);
		model.addAttribute("flights", flightService.getAllFlightsPaged(0));
		model.addAttribute("currentPage", 0);
		return "flights";
	}

	@GetMapping("passengers")
	public String showPassengerList(@RequestParam long flightId, Model model) {
		List<Passenger> passengers = flightService.getFlightById(flightId).getPassengers();
		model.addAttribute("passengers", passengers);
		model.addAttribute("flight", flightService.getFlightById(flightId));
		return "passengers";
	}

	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}

	@GetMapping("fancy")
	public String showLoginPage1() {
		return "index";
	}
	
	@PostMapping("/payment/new")
	public String payment(@Valid @ModelAttribute("passenger") Passenger passenger, BindingResult bindingResult,
		   Model model,@RequestParam("cardNumber") long cardNumber,@RequestParam("month") String month,
			@RequestParam("year") String year,@RequestParam("cvv") int cvv,@ModelAttribute Payment payment)  {
		
		//Flight flight = flightService.getFlightById(flightId);
		//Passenger passenger1 = passenger;
		//passenger1.setFlight(flight);
		//passengerService.savePassenger(passenger1);
		List<Payment> payList = paymentService.getAllByCardNumberAndMonthAndYearAndCvv(cardNumber,month,year,cvv);
		if(payList.isEmpty()) {
			model.addAttribute("invalidCard", "Sorry, Card is not Valid");
			return "payment";
		}
		else {
			Passenger pass = (Passenger) session.getAttribute("passenger");
			pass.setPayment(pass.getPayment());
			passengerService.savePassenger(pass);
			model.addAttribute("passenger", pass);
			model.addAttribute("payment", payList);
			return "confirmationPage";
		}
	}
	
	
	/*
	 * @PostMapping("/paid") public List<Payment>
	 * cardValidate(@RequestParam("cardNumber") long
	 * cardNumber,@RequestParam("month") String month,
	 * 
	 * @RequestParam("year") String year,@RequestParam("cvv") int
	 * cvv,@ModelAttribute Payment payment) { List<Payment> payList =
	 * paymentService.getAllByCardNumberAndMonthAndYearAndCvv(cardNumber,month,year,
	 * cvv); System.out.println(payList); if(payList.isEmpty())
	 * System.out.println("Sorry Payment not Verified......."); else
	 * System.out.println("You are In Payment Success,,,,,,,,,,,,,,"); return
	 * payList; }
	 */

}
