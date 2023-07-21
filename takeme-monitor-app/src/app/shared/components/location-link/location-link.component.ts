import { Component, OnInit, Input } from '@angular/core';
import { Location } from '../../models/location';

@Component({
  selector: 'app-location-link',
  templateUrl: './location-link.component.html',
  styleUrls: ['./location-link.component.scss']
})
export class LocationLinkComponent implements OnInit {

  @Input() location: Location;

  constructor() { }

  ngOnInit() {
  }

}
